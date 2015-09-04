package org.mlnlp.memnn.tensor;

/**
 * Abstract data element class used by Modules to pass information.
 *
 * @author jamesgung
 */
public abstract class Datum {

    public abstract void zero();

    public abstract int numEntries();

    public TableDatum asTable() {
        if (this instanceof TableDatum) {
            return (TableDatum) this;
        } else {
            return new TableDatum((TensorDatum) this);
        }
    }

    /**
     * Cast single-entry tables into tensors. Otherwise return as is.
     *
     * @return TensorDatum
     */
    public TensorDatum asTensor() {
        if (this instanceof TensorDatum) {
            return (TensorDatum) this;
        } else {
            TableDatum thisDatum = (TableDatum) this;
            if (numEntries() != 1) {
                throw new RuntimeException("Cannot cast multi-entry table to tensor.");
            } else {
                if (!(thisDatum.get(0) instanceof TensorDatum)) {
                    throw new RuntimeException("Cannot cast table to tensor.");
                }
                return (TensorDatum) thisDatum.get(0);
            }
        }
    }

}
