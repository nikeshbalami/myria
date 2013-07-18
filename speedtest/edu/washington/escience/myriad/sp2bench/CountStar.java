package edu.washington.escience.myriad.sp2bench;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.ImmutableList;

import edu.washington.escience.myriad.Schema;
import edu.washington.escience.myriad.TupleBatch;
import edu.washington.escience.myriad.Type;
import edu.washington.escience.myriad.operator.LocalJoin;
import edu.washington.escience.myriad.operator.QueryScan;
import edu.washington.escience.myriad.operator.RootOperator;
import edu.washington.escience.myriad.operator.SinkRoot;
import edu.washington.escience.myriad.operator.TBQueueExporter;
import edu.washington.escience.myriad.operator.agg.Aggregate;
import edu.washington.escience.myriad.operator.agg.Aggregator;
import edu.washington.escience.myriad.parallel.CollectConsumer;
import edu.washington.escience.myriad.parallel.CollectProducer;
import edu.washington.escience.myriad.parallel.ExchangePairID;

public class CountStar implements QueryPlanGenerator {

  final static ImmutableList<Type> outputTypes = ImmutableList.of(Type.LONG_TYPE, Type.LONG_TYPE);
  final static ImmutableList<String> outputColumnNames = ImmutableList.of("count(*) Dictionary", "count(*) Triples");
  final static Schema outputSchema = new Schema(outputTypes, outputColumnNames);

  final static ImmutableList<Type> countTypes = ImmutableList.of(Type.LONG_TYPE, Type.LONG_TYPE);
  final static ImmutableList<String> countColumnNames = ImmutableList.of("count", "dummy");

  final static Schema countSchema = new Schema(countTypes, countColumnNames);
  final ExchangePairID sendToMasterID = ExchangePairID.newID();

  @Override
  public Map<Integer, RootOperator[]> getWorkerPlan(int[] allWorkers) throws Exception {
    final ExchangePairID collectCountID = ExchangePairID.newID();

    final QueryScan countDictionary = new QueryScan("select count(*),0 from Dictionary", countSchema);
    final QueryScan countTriples = new QueryScan("select count(*),0 from Triples", countSchema);

    final LocalJoin countMergeJoin = new LocalJoin(countDictionary, countTriples, new int[] { 1 }, new int[] { 1 });

    final CollectProducer collectCountP = new CollectProducer(countMergeJoin, collectCountID, allWorkers[0]);
    final CollectConsumer collectCountC = new CollectConsumer(collectCountP.getSchema(), collectCountID, allWorkers);

    final Aggregate agg =
        new Aggregate(collectCountC, new int[] { 0, 2 }, new int[] { Aggregator.AGG_OP_SUM, Aggregator.AGG_OP_SUM });

    final CollectProducer sendToMaster = new CollectProducer(agg, sendToMasterID, 0);

    final Map<Integer, RootOperator[]> result = new HashMap<Integer, RootOperator[]>();
    result.put(allWorkers[0], new RootOperator[] { sendToMaster, collectCountP });

    for (int i = 1; i < allWorkers.length; i++) {
      result.put(allWorkers[i], new RootOperator[] { collectCountP });
    }

    return result;
  }

  @Override
  public RootOperator getMasterPlan(int[] allWorkers, final LinkedBlockingQueue<TupleBatch> receivedTupleBatches) {
    final CollectConsumer serverCollect =
        new CollectConsumer(outputSchema, sendToMasterID, new int[] { allWorkers[0] });
    TBQueueExporter queueStore = new TBQueueExporter(receivedTupleBatches, serverCollect);
    SinkRoot serverPlan = new SinkRoot(queueStore);
    return serverPlan;
  }

  public static void main(String[] args) throws Exception {
    System.out.println(new CountStar().getWorkerPlan(new int[] { 1, 2, 3, 4, 5 }));
  }
}
