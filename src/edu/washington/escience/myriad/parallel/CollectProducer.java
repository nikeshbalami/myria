package edu.washington.escience.myriad.parallel;

import org.jboss.netty.channel.Channel;

import edu.washington.escience.myriad.DbException;
import edu.washington.escience.myriad.Schema;
import edu.washington.escience.myriad.TupleBatch;
import edu.washington.escience.myriad.TupleBatchBuffer;
import edu.washington.escience.myriad.operator.Operator;
import edu.washington.escience.myriad.parallel.Exchange.ExchangePairID;
import edu.washington.escience.myriad.proto.TransportProto.TransportMessage;
import edu.washington.escience.myriad.util.IPCUtils;

/**
 * The producer part of the Collect Exchange operator.
 * 
 * The producer actively pushes the tuples generated by the child operator to the paired CollectConsumer.
 * 
 */
public final class CollectProducer extends Producer {

  /**
   * The working thread, which executes the child operator and send the tuples to the paired CollectConsumer operator.
   */
  class WorkingThread extends Thread {
    @Override
    public void run() {

      final Channel channel = getThisWorker().connectionPool.get(collectConsumerWorkerID, 3, null);

      ExchangePairID operatorID = operatorIDs[0];

      try {

        TupleBatchBuffer buffer = new TupleBatchBuffer(getSchema());

        TupleBatch tup = null;
        TransportMessage dm = null;
        while ((tup = child.next()) != null) {
          tup.compactInto(buffer);

          while ((dm = buffer.popFilledAsTM(operatorID)) != null) {
            channel.write(dm);
          }
        }

        while ((dm = buffer.popAnyAsTM(operatorID)) != null) {
          channel.write(dm);
        }

        channel.write(IPCUtils.eosTM(operatorID));

      } catch (final DbException e) {
        e.printStackTrace();
      }
    }
  }

  /** Required for Java serialization. */
  private static final long serialVersionUID = 1L;

  private transient WorkingThread runningThread;

  /**
   * The paired collect consumer address.
   */
  private final int collectConsumerWorkerID;

  private Operator child;

  public CollectProducer(final Operator child, final ExchangePairID operatorID, final int collectConsumerWorkerID) {
    super(operatorID);
    this.child = child;
    this.collectConsumerWorkerID = collectConsumerWorkerID;
  }

  @Override
  public void cleanup() {
  }

  @Override
  protected TupleBatch fetchNext() throws DbException {
    try {
      // wait until the working thread terminate and return an empty tuple set
      runningThread.join();
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public Operator[] getChildren() {
    return new Operator[] { child };
  }

  @Override
  public Schema getSchema() {
    return child.getSchema();
  }

  @Override
  public void init() throws DbException {
    runningThread = new WorkingThread();
    runningThread.start();
  }

  @Override
  public void setChildren(final Operator[] children) {
    child = children[0];
  }

  @Override
  public TupleBatch fetchNextReady() throws DbException {
    return fetchNext();
  }

}
