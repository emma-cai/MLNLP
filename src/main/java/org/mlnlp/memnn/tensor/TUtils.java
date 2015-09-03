package org.mlnlp.memnn.tensor;

/**
 * Convenience methods for checking dimensionality constraints.
 *
 * @author jamesgung
 */
public class TUtils {

    public static void checkSizeAndShape(Tensor tensor1, Tensor tensor2) {
        checkSize(tensor1, tensor2);
        checkDimensionality(tensor1, tensor2);
        for (int i = 0; i < tensor1.dims; ++i) {
            if (tensor1.shape[i] != tensor2.shape[i]) {
                throw new IllegalArgumentException("Tensor shapes not equal for dimension " + i + ": "
                        + tensor1.shape[i] + " vs. " + tensor2.shape[i]);
            }
        }
    }

    public static void checkDimensionality(Tensor tensor1, Tensor tensor2) {
        if (tensor1.dims != tensor2.dims) {
            throw new IllegalArgumentException("Tensor order not equal: " + tensor1.dims + " vs. " + tensor2.dims);
        }
    }

    public static void checkDimensionality(Tensor tensor1, Tensor tensor2, int dim1, int dim2) {
        if (tensor1.shape[dim1] != tensor2.shape[dim2]) {
            throw new IllegalArgumentException("Tensor dim size not equal: " + tensor1.shape[dim1] + " vs. "
                    + tensor2.shape[dim2]);
        }
    }

    public static void checkSize(Tensor tensor1, Tensor tensor2) {
        if (tensor1.size != tensor2.size) {
            throw new IllegalArgumentException("Tensor sizes not equal: " + tensor1.size + " vs. " + tensor2.size);
        }
    }

    public static void checkDimensionality(Tensor tensor1, int dim) {
        if (tensor1.dims != dim) {
            throw new IllegalArgumentException("Tensor order not correct: required " + dim + " vs. actual " + tensor1.dims);
        }
    }

    public static void checkArgument(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

}
