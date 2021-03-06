package edu.washington.escience.myria.operator.agg;

import com.google.common.collect.ImmutableSet;

import edu.washington.escience.myria.Type;
import edu.washington.escience.myria.storage.AppendableTable;
import edu.washington.escience.myria.storage.MutableTupleBuffer;
import edu.washington.escience.myria.storage.ReadableColumn;
import edu.washington.escience.myria.storage.ReplaceableColumn;
import edu.washington.escience.myria.storage.TupleBatch;

/**
 * Knows how to compute some aggregates over a FloatColumn.
 */
public final class FloatAggregator extends PrimitiveAggregator {

  protected FloatAggregator(final String inputName, final int column, final AggregationOp aggOp) {
    super(inputName, column, aggOp);
  }

  /** Required for Java serialization. */
  private static final long serialVersionUID = 1L;

  @Override
  public void addRow(
      final TupleBatch from,
      final int fromRow,
      final MutableTupleBuffer to,
      final int toRow,
      final int offset) {
    ReadableColumn fromCol = from.asColumn(column);
    ReplaceableColumn toCol = to.getColumn(offset, toRow);
    final int inColumnRow = to.getInColumnIndex(toRow);
    switch (aggOp) {
      case COUNT:
        toCol.replaceLong(toCol.getLong(inColumnRow) + 1, inColumnRow);
        break;
      case MAX:
        toCol.replaceFloat(
            Math.max(fromCol.getFloat(fromRow), toCol.getFloat(inColumnRow)), inColumnRow);
        break;
      case MIN:
        toCol.replaceFloat(
            Math.min(fromCol.getFloat(fromRow), toCol.getFloat(inColumnRow)), inColumnRow);
        break;
      case SUM:
        toCol.replaceDouble(fromCol.getFloat(fromRow) + toCol.getDouble(inColumnRow), inColumnRow);
        break;
      case SUM_SQUARED:
        toCol.replaceDouble(
            (double) fromCol.getFloat(fromRow) * fromCol.getFloat(fromRow)
                + toCol.getDouble(inColumnRow),
            inColumnRow);
        break;
      default:
        throw new IllegalArgumentException(aggOp + " is invalid");
    }
  }

  @Override
  protected boolean isSupported(final AggregationOp aggOp) {
    return ImmutableSet.of(
            AggregationOp.COUNT,
            AggregationOp.MIN,
            AggregationOp.MAX,
            AggregationOp.SUM,
            AggregationOp.AVG,
            AggregationOp.STDEV,
            AggregationOp.SUM_SQUARED)
        .contains(aggOp);
  }

  @Override
  protected Type getOutputType() {
    switch (aggOp) {
      case COUNT:
        return Type.LONG_TYPE;
      case MAX:
      case MIN:
        return Type.FLOAT_TYPE;
      case SUM:
      case AVG:
      case STDEV:
        return Type.DOUBLE_TYPE;
      default:
        throw new IllegalArgumentException("Type " + aggOp + " is invalid");
    }
  };

  @Override
  public void appendInitValue(AppendableTable data, final int column) {
    switch (aggOp) {
      case COUNT:
        data.putLong(column, 0);
        break;
      case SUM:
      case SUM_SQUARED:
        data.putDouble(column, 0);
        break;
      case MAX:
        data.putFloat(column, Float.MIN_VALUE);
        break;
      case MIN:
        data.putFloat(column, Float.MAX_VALUE);
        break;
      default:
        throw new IllegalArgumentException("Type " + aggOp + " is invalid");
    }
  }
}
