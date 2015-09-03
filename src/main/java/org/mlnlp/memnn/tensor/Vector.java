package org.mlnlp.memnn.tensor;

/**
 * First order Tensor convenience class.
 *
 * @author jamesgung
 */
public class Vector extends Tensor {

    public Vector(int size) {
        super(new int[]{size}, new int[]{1});
    }

    public Vector(double[] values) {

        super(values, new int[]{values.length}, new int[]{1});
    }

    public Vector(int offset, int end, double[] values) {

        super(offset, values, new int[]{end - offset}, new int[]{1});
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < end; ++i) {
            sb.append(values[i]).append(" ");
        }
        return sb.toString();
    }

}
