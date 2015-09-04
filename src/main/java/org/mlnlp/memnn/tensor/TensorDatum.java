package org.mlnlp.memnn.tensor;

/**
 * Datum containing a single Tensor.
 *
 * @author jamesgung
 */
public class TensorDatum extends Datum {

    public Tensor tensor;

    public TensorDatum() {
        this.tensor = new Tensor();
    }

    public TensorDatum(Tensor tensor) {
        this.tensor = tensor;
    }

    @Override
    public void zero() {
        tensor.zero();
    }

    @Override
    public int numEntries() {
        return 1;
    }

    public void setTensor(Tensor tensor) {
        this.tensor = tensor;
    }

    public Tensor tensor() {
        return tensor;
    }
}
