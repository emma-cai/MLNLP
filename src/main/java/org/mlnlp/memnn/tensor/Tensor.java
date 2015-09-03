package org.mlnlp.memnn.tensor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Simple Tensor implementation (based on ndarrays of Numpy). An Nth order tensor consists of a contiguous 1d segment of memory,
 * combined with a particular indexing scheme that maps N integers onto locations in this memory segment.  The ranges of the index
 * integers are controlled by the shape of the tensor.
 *
 * @author jamesgung
 */
public class Tensor {

    public static final Random random = new Random(123);
    public double[] values;
    public int[] shape; // shape of tensor (the size of each axis/dimension/order of the tensor)
    protected int[] strides; // skip distance between consecutive elements of each axis

    public int getDims() {
        return dims;
    }

    protected int dims; // order/# of dimensions of the tensor
    protected int size;

    public int offset;
    protected int end;

    public Tensor(int offset, double[] values, int[] shape, int[] strides) {
        this.offset = offset;
        this.size = values.length > 0 ? size(shape) : 0;
        this.end = size + offset;
        this.values = values;
        this.shape = shape;
        this.dims = shape.length;
        this.strides = strides;
    }

    public Tensor(double[] values, int[] shape, int[] strides) {
        this(0, values, shape, strides);
    }

    public Tensor(int[] shape, int[] strides) {

        this(new double[size(shape)], shape, strides);
    }

    /**
     * Flatten a 2d array into a 1d array representation.
     *
     * @param values 2d array
     */
    public Tensor(double[][] values) {
        int rows = values.length;
        int cols;
        if (rows == 0) {
            cols = 0;
        } else {
            cols = values[0].length;
        }
        this.size = rows * cols;
        this.shape = new int[]{rows, cols};
        this.dims = 2;
        this.strides = new int[]{cols, 1};
        this.end = size;
        this.offset = 0;
        this.values = new double[size];
        for (int i = 0; i < rows; ++i) {
            System.arraycopy(values[i], 0, this.values, i * cols, cols);
        }
    }

    public Tensor() {
        this(0, new double[0], new int[0], new int[0]);
    }

    public Tensor(Tensor tensor) {
        this(tensor.offset, tensor.values.clone(), tensor.shape.clone(), tensor.strides.clone());
    }

    /**
     * If not the same size and shape as argument tensor, change size (and possibly storage) to match.
     *
     * @param tensor to resize as
     */
    public Tensor resizeAs(Tensor tensor) {
        if (!sameSize(tensor)) {
            this.offset = 0;
            this.shape = tensor.shape.clone();
            this.strides = tensor.strides.clone();
            this.dims = tensor.dims;
            if (size < tensor.size) {
                this.values = new double[tensor.size];
            }
            this.size = tensor.size;
            this.end = size;
        }
        return this;
    }

    public Tensor resize2d(int size1, int size2) {
        return resize(2, new int[]{size1, size2}, new int[]{size2, 1});
    }

    public Tensor resize3d(int size1, int size2, int size3) {
        return resize(3, new int[]{size1, size2, size3}, new int[]{size3 * size2, size3, 1});
    }

    public Tensor resize4d(int size1, int size2, int size3, int size4) {
        return resize(4, new int[]{size1, size2, size3, size4}, new int[]{size4 * size3 * size2, size4 * size3, size4, 1});
    }

    /**
     * Resize a tensor.
     *
     * @param dims    number of dimensions
     * @param shape   shape of tensor
     * @param strides strides of each dimension
     * @return resized tensor
     */
    public Tensor resize(int dims, int[] shape, int[] strides) {
        if (!sameSize(shape)) {
            this.dims = dims;
            this.shape = shape.clone();
            this.strides = strides.clone();
            this.offset = 0;
            int size = size(shape);
            if (this.size < size) {
                this.values = new double[size];
            }
            this.size = size;
            this.end = size;
        }
        return this;
    }

    /**
     * Resize a tensor.
     *
     * @param shape shape of tensor
     * @return resized tensor
     */
    public Tensor resize(int[] shape) {
        return resize(shape.length, shape, getStrides(shape));
    }

    public Tensor copy(Tensor tensor) {
        System.arraycopy(tensor.values, tensor.offset, values, offset, tensor.size);
        return this;
    }

    public Tensor resizeAndCopy(Tensor tensor) {
        resizeAs(tensor);
        copy(tensor);
        return this;
    }

    public void flatten() {

        int size = size(shape);
        this.shape = new int[]{size};
        this.strides = new int[]{1};
        this.dims = 1;

    }

    public void set(double[] values, int offset, int[] shape, int[] strides, int dims) {
        this.values = values;
        this.offset = offset;
        this.shape = shape.clone();
        this.strides = strides.clone();
        this.size = values.length > 0 ? size(shape) : 0;
        this.end = offset + size;
        this.dims = dims;
    }

    public Tensor view(int[] shape) {

        return new Tensor(this.values, shape, getStrides(shape));

    }

    public static int[] getStrides(int[] shape) {
        int size = 1;
        int[] strides = new int[shape.length];
        for (int i = shape.length - 1; i >= 0; --i) {
            strides[i] = size;
            size *= shape[i];
        }
        return strides;
    }

    public double[] getDenseArray() {
        double[] array = new double[size];
        System.arraycopy(this.values, offset, array, 0, size);
        return array;
    }

    public static int size(int... shape) {

        int size = 1;
        for (int order : shape) {
            size *= order;
        }
        return size;
    }

    public int getIndex(int... indices) {

        int index = offset;
        for (int i = 0; i < indices.length; ++i) {
            index += indices[i] * getStride(i);
        }
        return index;
    }

    public double get(int index) {

        return values[offset + index];
    }

    public double get(int... indices) {

        return values[getIndex(indices)];
    }

    public void set(double value, int index) {

        values[offset + index] = value;
    }

    public void set(double value, int... indices) {

        values[getIndex(indices)] = value;
    }

    public int getDim(int dim) {

        return shape[dim];
    }

    public int getStride(int dim) {

        return strides[dim];
    }

    public int getSize() {
        return size;
    }

    public Tensor initGaussian(double mean, double stdev, Random random) {

        for (int i = offset; i < end; ++i) {
            values[i] = (random.nextGaussian() * stdev + mean);
        }
        return this;
    }

    public Tensor initGaussian(double mean, double stdev) {
        return initGaussian(mean, stdev, random);
    }

    public Tensor initGaussian() {
        return initGaussian(0, 1);
    }

    public Tensor initRandomUniform(Random random) {

        for (int i = offset; i < end; ++i) {
            values[i] = random.nextDouble();
        }
        return this;
    }

    public Tensor initRandomUniform(double start, double end, Random random) {
        if (end <= start) {
            throw new IllegalArgumentException("Illegal range: [" + start + ", " + end + "]");
        }
        initRandomUniform(random);
        double range = (end - start);
        mult(range);
        add(start);
        return this;
    }

    public Tensor initRandomUniform(double start, double end) {
        return initRandomUniform(start, end, random);
    }

    public Tensor initRandomUniform() {
        return initRandomUniform(random);
    }

    public void initBernoulli(double prob, Random random) {
        for (int i = offset; i < end; ++i) {
            if (random.nextDouble() > prob) {
                values[i] = 0;
            } else {
                values[i] = 1;
            }
        }
    }

    public void initBernoulli(double prob) {
        initBernoulli(prob, random);
    }

    /**
     * Calculates the Frobenius norm (square root of sum of all elements).
     *
     * @return Frobenius norm of this matrix
     */
    public double frobeniusNorm() {

        double sum = 0.0;
        for (int i = offset; i < end; ++i) {
            sum += values[i] * values[i];
        }
        return Math.sqrt(sum);
    }

    public double sum() {
        double sum = 0.0;
        for (int i = offset; i < end; ++i) {
            sum += values[i];
        }
        return sum;
    }

    public double mean() {
        return sum() / size;
    }

    public double stdev(boolean unbiased) {
        double mean = mean();
        double sumsq = 0;
        for (int i = offset; i < end; ++i) {
            double diff = values[i] - mean;
            sumsq += diff * diff;
        }
        return Math.sqrt(sumsq / (unbiased ? size - 1 : size));
    }

    public double stdev() {
        return stdev(true);
    }

    public boolean sameSize(int[] shape) {
        if (shape.length != dims) {
            return false;
        }
        for (int i = 0; i < dims; ++i) {
            if (this.shape[i] != shape[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean sameSize(Tensor tensor) {
        return sameSize(tensor.shape);
    }

    public void clip(double min, double max) {

        for (int i = offset; i < end; ++i) {
            if (values[i] < min) {
                values[i] = max;
            } else if (values[i] > max) {
                values[i] = max;
            }
        }

    }

    public Tensor fill(int offset, int size, double value) {
        for (int i = offset; i < offset + size; ++i) {
            values[i] = value;
        }
        return this;
    }

    public Tensor fill(double value) {
        for (int i = offset; i < end; ++i) {
            values[i] = value;
        }
        return this;
    }

    public Tensor zero() {
        return fill(0.0);
    }

    public Tensor sqrt() {
        for (int i = offset; i < end; ++i) {
            values[i] = Math.sqrt(values[i]);
        }
        return this;
    }

    public Tensor mult(double constant) {
        for (int i = offset; i < end; ++i) {
            values[i] *= constant;
        }
        return this;
    }

    public Tensor div(double constant) {
        for (int i = offset; i < end; ++i) {
            values[i] /= constant;
        }
        return this;
    }

    public Tensor multH(Tensor tensor) {
        int off = tensor.offset - offset;
        for (int i = offset; i < end; ++i) {
            values[i] *= tensor.values[i + off];
        }
        return this;
    }

    public Tensor addHadamardProduct(Tensor tensor1, Tensor tensor2, double scale) {
        TUtils.checkSizeAndShape(tensor1, tensor2);
        TUtils.checkSizeAndShape(this, tensor1);
        int off1 = tensor1.offset - offset;
        int off2 = tensor2.offset - offset;

        for (int i = offset; i < end; ++i) {
            values[i] += tensor1.values[i + off1] * tensor2.values[i + off2] * scale;
        }
        return this;
    }

    public Tensor divH(Tensor tensor) {
        int off = tensor.offset - offset;
        for (int i = offset; i < end; ++i) {
            values[i] /= tensor.values[i + off];
        }
        return this;
    }

    public Tensor add(double constant) {
        for (int i = offset; i < end; ++i) {
            values[i] += constant;
        }
        return this;
    }

    public Tensor add(Tensor addition) {
        TUtils.checkSizeAndShape(this, addition);
        int additionOffset = addition.offset - offset;
        for (int i = offset; i < end; ++i) {
            values[i] += addition.values[i + additionOffset];
        }
        return this;
    }

    public Tensor sub(Tensor subtraction) {
        TUtils.checkSizeAndShape(this, subtraction);
        int additionOffset = subtraction.offset - offset;
        for (int i = offset; i < end; ++i) {
            values[i] -= subtraction.values[i + additionOffset];
        }
        return this;
    }

    public Tensor add(Tensor addition, double scale) {
        TUtils.checkSizeAndShape(this, addition);
        int additionOffset = addition.offset - offset;
        for (int i = offset; i < end; ++i) {
            values[i] += addition.values[i + additionOffset] * scale;
        }
        return this;
    }

    public double dotProduct(Tensor input) {
        TUtils.checkSizeAndShape(this, input);
        double sum = 0;
        int off = input.offset - offset;
        for (int i = offset; i < end; ++i) {
            sum += values[i] * input.values[i + off];
        }
        return sum;
    }

    public double sumsq(double mean) {
        double sum = 0;
        for (int i = offset; i < end; ++i) {
            double diff = values[i] - mean;
            sum += diff * diff;
        }
        return sum;
    }

    public Tensor log() {
        for (int i = offset; i < end; ++i) {
            values[i] = Math.log(values[i]);
        }
        return this;
    }

    public Tensor sigmoid() {
        for (int i = offset; i < end; ++i) {
            values[i] = 1.0 / (1 + Math.exp(-values[i]));
        }
        return this;
    }

    public Tensor exp() {
        for (int i = offset; i < end; ++i) {
            values[i] = Math.exp(values[i]);
        }
        return this;
    }

    /**
     * Add the outer product of two vectors to this d1 x d2 matrix.
     *
     * @param vector1 vector of dimensionality d1
     * @param vector2 vector of dimensionality d2
     * @param scale   scaling factor
     */
    public Tensor addOuterProduct(Tensor vector1, Tensor vector2, double scale) {
        TUtils.checkDimensionality(vector1, 1);
        TUtils.checkDimensionality(vector2, 1);
        TUtils.checkDimensionality(vector1, this, 0, 0);
        TUtils.checkDimensionality(vector2, this, 0, 1);
        for (int row = 0; row < vector1.size; ++row) {
            int rowOffset = row * strides[0] + offset;
            double rowVal = vector1.values[vector1.offset + row];
            for (int col = 0; col < vector2.size; ++col) {
                this.values[rowOffset + col * strides[1]] = rowVal * vector2.values[vector2.offset + col] * scale;
            }
        }
        return this;
    }

    /**
     * Add the result of multiplying a matrix M by a vector V to this tensor. M in (c1, c2) and v in (c2, 1) resulting in a vector
     * (c1, 1).
     *
     * @param matrix 2nd order tensor (dim1, dim2)
     * @param vector 1st order tensor (dim2)
     */
    public Tensor addmv(Tensor matrix, Tensor vector) {
        TUtils.checkDimensionality(matrix, 2);
        TUtils.checkDimensionality(vector, 1);
        TUtils.checkDimensionality(matrix, vector, 1, 0);
        TUtils.checkDimensionality(matrix, this, 0, 0);
        for (int row = 0; row < matrix.shape[0]; ++row) {
            int rowOffset = row * matrix.strides[0] + matrix.offset;
            for (int col = 0; col < matrix.shape[1]; ++col) {
                values[row + offset] += matrix.values[rowOffset + col * matrix.strides[1]] * vector.values[vector.offset + col];
            }
        }
        return this;
    }

    public Tensor add(int offset, Tensor addition, int additionOffset, int size) {
        for (int i = 0; i < size; ++i) {
            this.values[i + offset] += addition.values[i + additionOffset];
        }
        return this;
    }

    /**
     * Add the result of multiplying a matrix M1 by a Matrix M2 to this tensor. M1 in (c1, c2) and M2 in (c2, c3) resulting in a
     * matrix (c1, c3).
     *
     * @param matrix1 2nd order tensor (dim1, dim2)
     * @param matrix2 2st order tensor (dim2, dim3)
     */
    public Tensor addmm(Tensor matrix1, Tensor matrix2) {
        TUtils.checkDimensionality(matrix1, 2);
        TUtils.checkDimensionality(matrix2, 2);

        TUtils.checkDimensionality(matrix1, matrix2, 1, 0);
        TUtils.checkDimensionality(matrix1, this, 0, 0);
        final int size = matrix1.getDim(1);
        final int dim1 = matrix1.getDim(0);
        final int dim2 = matrix2.getDim(1);

        final int stride1 = matrix2.getStride(1);
        final int stride2 = matrix2.getStride(0);
        final int thisStride = this.strides[0];

        for (int m1row = 0; m1row < dim1; ++m1row) {
            int m1Offset = matrix1.offset + m1row * matrix1.strides[0];
            int offset = this.offset + (m1row * thisStride);
            for (int m2row = 0; m2row < dim2; ++m2row) {
                int m2Offset = matrix2.offset + m2row * stride1;
                for (int n = 0; n < size; ++n) {
                    values[offset + m2row] +=
                            matrix1.values[m1Offset + n * matrix1.strides[1]] * matrix2.values[m2Offset + n * stride2];
                }
            }
        }

        return this;
    }

    /**
     * Add the result of multiplying a matrix M1 by a Matrix M2 to this tensor. M1 in (c1, c2) and M2 in (c2, c3) resulting in a
     * matrix (c1, c3).
     *
     * @param matrix1 2nd order tensor (dim1, dim2)
     * @param matrix2 2st order tensor (dim2, dim3)
     * @param scale   scaling factor
     */
    public Tensor addmm(Tensor matrix1, Tensor matrix2, double scale) {
        TUtils.checkDimensionality(matrix1, 2);
        TUtils.checkDimensionality(matrix2, 2);

        TUtils.checkDimensionality(matrix1, matrix2, 1, 0);
        TUtils.checkDimensionality(matrix1, this, 0, 0);
        final int size = matrix1.getDim(1);
        final int dim1 = matrix1.getDim(0);
        final int dim2 = matrix2.getDim(1);

        final int stride1 = matrix2.getStride(1);
        final int stride2 = matrix2.getStride(0);
        final int thisStride = this.strides[0];

        for (int m1row = 0; m1row < dim1; ++m1row) {
            int m1Offset = matrix1.offset + m1row * matrix1.strides[0];
            int offset = this.offset + (m1row * thisStride);
            for (int m2row = 0; m2row < dim2; ++m2row) {
                int m2Offset = matrix2.offset + m2row * stride1;
                for (int n = 0; n < size; ++n) {
                    values[offset + m2row] +=
                            matrix1.values[m1Offset + n * matrix1.strides[1]] * matrix2.values[m2Offset + n * stride2] * scale;
                }
            }
        }

        return this;
    }

    /**
     * Returns a Matrix over the same memory block, with a new indexing scheme to result in transpose.
     *
     * @return Transpose of this Matrix
     */
    public Matrix transpose() {
        TUtils.checkDimensionality(this, 2);
//        if (strides[1] != 1) {
//            throw new RuntimeException();
//        }

//        if (strides[0] != shape[1]) {
//            throw new RuntimeException();
//        }
        return new Matrix(offset, values, shape[1], shape[0], strides[1], strides[0]);
    }

    /**
     * Return a new Tensor that is a tensor slice at the given index in the given dimension. The returned tensor has one fewer
     * dimensions (one dimension is removed). It is not possible to select() on vectors (1D tensors).
     *
     * @param dimension  dimension to slice
     * @param sliceIndex index of dimension to slice
     * @return slice of original tensor
     */
    public Tensor select(int dimension, int sliceIndex) {
        TUtils.checkArgument(dims > 1, "cannot select on a vector");
        TUtils.checkArgument(dimension >= 0, "invalid dimension (out of range)");
        TUtils.checkArgument(sliceIndex >= 0, "invalid slice (out of range)");
        Tensor newTensor = this.narrow(dimension, sliceIndex, 1);
        for (int dim = dimension; dim < newTensor.dims - 1; ++dim) {
            newTensor.shape[dim] = newTensor.shape[dim + 1];
            newTensor.strides[dim] = newTensor.strides[dim + 1];
        }
        newTensor.dims--;
        int[] shape = new int[newTensor.dims];
        int[] strides = new int[newTensor.dims];
        for (int i = 0; i < newTensor.dims; ++i) {
            shape[i] = newTensor.shape[i];
            strides[i] = newTensor.strides[i];
        }
        newTensor.shape = shape;
        newTensor.strides = strides;
        return newTensor;
    }

    /**
     * Return a new tensor that is a narrowed version of the original tensor (dimension is narrowed from firstIndex to (firstIndex +
     * size - 1).
     *
     * @param dimension  dimension to narrow
     * @param firstIndex begin index
     * @param size       size of resulting narrowed dimension
     * @return narrowed tensor
     */
    public Tensor narrow(int dimension, int firstIndex, int size) {

        TUtils.checkArgument(dimension >= 0 && dimension < dims, "out of range");
        TUtils.checkArgument(firstIndex >= 0 && firstIndex < shape[dimension], "out of range: " + firstIndex);
        TUtils.checkArgument(size > 0 && (firstIndex + size <= shape[dimension]), "out of range");
        Tensor newTensor = new Tensor();
        newTensor.set(values, offset, shape, strides, dims);

        if (firstIndex > 0) {
            newTensor.offset += firstIndex * newTensor.strides[dimension];
        }
        newTensor.end = newTensor.offset + size * newTensor.strides[dimension];
        newTensor.shape[dimension] = size;
        newTensor.size = newTensor.end - newTensor.offset;
        return newTensor;

    }

    /**
     * Returns true if elements of this tensor are contiguous in memory.
     *
     * @return true if elements are contiguous
     */
    public boolean isContiguous() {
        int contiguousStrideSize = 1;
        for (int dim = dims - 1; dim >= 0; --dim) {
            if (shape[dim] != 1) {
                if (strides[dim] == contiguousStrideSize) {
                    contiguousStrideSize *= shape[dim];
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Copy elements of source into this tensor by selecting indices given in input.
     *
     * @param dim     dimension
     * @param indices indices on dimension to copy
     * @param source  tensor to be copied
     */
    public void copyIndex2D(int dim, Tensor indices, Tensor source) {
        TUtils.checkArgument(indices.getDims() == 1, "Index must be a vector.");
        TUtils.checkArgument(dim < source.getDims(), "Index is out of bounds.");
        if (!indices.isContiguous()) {
            indices.resizeAndCopy(indices);
        }
        int numElements = indices.size;
        this.resize2d(numElements, source.shape[dim + 1]);
        if (this.getDims() > 1) {
            for (int i = 0; i < numElements; ++i) {
                int index = (int) indices.values[i];
                Tensor targetSlice = this.select(dim, i);
                Tensor sourceSlice = source.select(dim, index);
                targetSlice.copy(sourceSlice);
            }
        }
    }

    /**
     * Project weights onto surface of a ball with radius l2Constraint if norm constraint is violated. Max-norm regularization.
     *
     * @param l2Constraint Radius of ball.
     */
    public final void constrainL2Norm(double l2Constraint) {

        if (getDims() == 0) {
            return;
        }

        TUtils.checkArgument(getDims() == 1 || getDims() == 2, "Illegal parameter dimensionality: " + getDims());

        if (getDims() == 1) {
            clip(-l2Constraint, l2Constraint);
        } else if (getDims() == 2) {
            for (int i = 0; i < shape[0]; ++i) {
                Tensor row = select(0, i);
                double colNorm = row.frobeniusNorm();
                double desired;
                if (colNorm > l2Constraint) {
                    desired = l2Constraint;
                } else {
                    desired = colNorm;
                }
                double scale = 1e-12 + desired / (1e-12 + colNorm);
                row.mult(scale);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < end; ++i) {
            sb.append(values[i]).append(" ");
        }
        return "[" + sb.toString().trim().replace(" ", ", ") + "]";
    }


    /**
     * Flatten a list of tensors into a single tensor. Associates each tensor in list with storage of new flattened tensor.
     *
     * @param tensors to be flattened
     * @return flat tensor
     */
    public static Tensor flatten(List<Tensor> tensors) {

        Map<double[], Integer> storages = new HashMap<>();
        int numParams = 0;
        for (Tensor tensor : tensors) {
            if (!storages.containsKey(tensor.values)) {
                storages.put(tensor.values, numParams);
                numParams += tensor.size;
            }
        }

        Tensor flat = new Vector(numParams);
        double[] values = flat.values;
        for (Tensor tensor : tensors) {
            int offset = storages.get(tensor.values);
            System.arraycopy(tensor.values, tensor.offset, values, offset, tensor.size);
            tensor.set(values, offset, tensor.shape, tensor.strides, tensor.dims);
        }

        return flat;
    }

    public int getMaxIndex() {
        double max = -Double.MAX_VALUE;
        int maxIndex = offset;
        for (int i = offset; i < end; ++i) {
            double val = values[i];
            if (val >= max) {
                maxIndex = i;
                max = val;
            }
        }
        return maxIndex;
    }


    // All following methods are implemented by Qingqing Cai.
    /**
     *
     */
    public Tensor innerDotProduct(Tensor matrix1, Tensor matrix2) {
        TUtils.checkDimensionality(matrix1, 2);
        TUtils.checkDimensionality(matrix2, 2);

        TUtils.checkDimensionality(matrix1, matrix2, 1, 0);
        TUtils.checkDimensionality(matrix1, this, 0, 0);
        final int size = matrix1.getDim(1);
        final int dim1 = matrix1.getDim(0);
        final int dim2 = matrix2.getDim(1);

        final int stride1 = matrix2.getStride(1);
        final int stride2 = matrix2.getStride(0);
        final int thisStride = this.strides[0];

        for (int m1row = 0; m1row < dim1; ++m1row) {
            int m1Offset = matrix1.offset + m1row * matrix1.strides[0];
            int offset = this.offset + (m1row * thisStride);
            for (int m2row = 0; m2row < dim2; ++m2row) {
                int m2Offset = matrix2.offset + m2row * stride1;
                for (int n = 0; n < size; ++n) {
                    values[offset + m2row] +=
                            matrix1.values[m1Offset + n * matrix1.strides[1]] * matrix2.values[m2Offset + n * stride2];
                }
            }
        }

        return this;
    }

}
