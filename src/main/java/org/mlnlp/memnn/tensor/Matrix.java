package org.mlnlp.memnn.tensor;

/**
 * Matrix (second order Tensor) implementation with matrix-specific functionality.
 *
 * @author jamesgung
 */
public class Matrix extends Tensor {

    public Matrix(int offset, double[] values, int rows, int cols, int rowStride, int colStride) {

        super(offset, values, new int[]{rows, cols}, new int[]{rowStride, colStride});
    }

    public Matrix(double[] values, int rows, int cols) {

        super(values, new int[]{rows, cols}, new int[]{cols, 1});
    }

    public Matrix(int offset, double[] values, int rows, int cols) {

        super(offset, values, new int[]{rows, cols}, new int[]{cols, 1});
    }

    public Matrix(int rows, int cols) {

        super(new int[]{rows, cols}, new int[]{cols, 1});
    }

    /**
     * Return row-wise representation of matrix.
     *
     * @return pretty-print of matrix
     */
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getDim(0); ++i) {
            sb.append("[");
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < getDim(1); ++j) {
                line.append(get(i, j)).append(" ");
            }
            sb.append(line.toString().trim().replace(" ", ", "));
            sb.append("]\n");
        }
        return "[" + sb.toString().trim() + "]";
    }

}
