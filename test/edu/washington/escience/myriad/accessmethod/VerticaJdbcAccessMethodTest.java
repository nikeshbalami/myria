/**
 * 
 */
package edu.washington.escience.myriad.accessmethod;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import edu.washington.escience.myriad.MyriaConstants;
import edu.washington.escience.myriad.RelationKey;
import edu.washington.escience.myriad.Schema;
import edu.washington.escience.myriad.TupleBatch;
import edu.washington.escience.myriad.TupleBatchBuffer;
import edu.washington.escience.myriad.Type;
import edu.washington.escience.myriad.operator.JdbcInsert;
import edu.washington.escience.myriad.operator.QueryScan;
import edu.washington.escience.myriad.operator.TupleSource;

public class VerticaJdbcAccessMethodTest {

  /* Test data */
  private TupleBatchBuffer buffer;
  private Schema schema;
  private RelationKey relationKey;
  private final static int NUM_TUPLES = 2 * TupleBatch.BATCH_SIZE + 1;

  /* Connection information */
  private final String host = "dbserver05.cs.washington.edu";
  private final int port = 15433;
  private final String user = "dbadmin";
  private final String password = "mrbenchmarks";
  private final String dbms = MyriaConstants.STORAGE_SYSTEM_VERTICA;
  private final String databaseName = "mrbenchmarks";
  private final String jdbcDriverName = "com.vertica.jdbc.Driver";
  private final JdbcInfo jdbcInfo = JdbcInfo.of(jdbcDriverName, dbms, host, port, databaseName, user, password);

  @Before
  public void createTupleBatchBuffers() {
    if (schema == null) {
      schema = Schema.of(ImmutableList.of(Type.INT_TYPE, Type.STRING_TYPE), ImmutableList.of("value", "name"));
      relationKey = RelationKey.of("test", "test", "big");
    }
    if (buffer == null || buffer.numTuples() < NUM_TUPLES) {
      buffer = new TupleBatchBuffer(schema);
      for (int i = 0; i < NUM_TUPLES; ++i) {
        buffer.put(0, i);
        buffer.put(1, String.valueOf(i) + " test");
      }
    }
  }

  @Test
  public void testCreateTableAndCountMultipleBatches() throws Exception {
    /* Insert the NUM_TUPLES tuples */
    TupleSource source = new TupleSource(buffer);
    JdbcInsert insert = new JdbcInsert(source, relationKey, jdbcInfo);
    insert.open(null);
    while (!insert.eos()) {
      insert.nextReady();
    }
    insert.close();

    /* Count them and make sure we got the right count. */
    QueryScan count =
        new QueryScan("SELECT COUNT(*) FROM " + relationKey.toString(jdbcInfo.getDbms()), Schema.of(ImmutableList
            .of(Type.LONG_TYPE), ImmutableList.of("count")));

    HashMap<String, Object> localEnvVars = new HashMap<String, Object>();
    localEnvVars.put(MyriaConstants.EXEC_ENV_VAR_DATABASE_SYSTEM, MyriaConstants.STORAGE_SYSTEM_MONETDB);
    localEnvVars.put(MyriaConstants.EXEC_ENV_VAR_DATABASE_CONN_INFO, jdbcInfo);
    final ImmutableMap<String, Object> execEnvVars = ImmutableMap.copyOf(localEnvVars);

    count.open(execEnvVars);
    TupleBatch result = count.nextReady();
    assertTrue(result != null);
    assertTrue(result.getLong(0, 0) == NUM_TUPLES);
    result = count.nextReady();
    assertTrue(result == null);
    assertTrue(count.eos());
    count.close();
  }
}