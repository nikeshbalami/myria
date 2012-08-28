package edu.washington.escience.myriad;

import java.nio.LongBuffer;

public class LongColumn extends Column {
  LongBuffer data;

  public LongColumn() {
    this.data = LongBuffer.allocate(TupleBatch.BATCH_SIZE);
  }

  public long getLong(int index) {
    return data.get(index);
  }

  public void putLong(long value) {
    data.put(value);
  }
}