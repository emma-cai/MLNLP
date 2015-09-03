package org.mlnlp.memnn.tensor;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by qingqingcai on 9/3/15.
 */
public class MatrixTest {

    public static final double delta = 1e-10;

    @Test
    public void testAddmm() {
        double[] array1 = {0.74248796, 0.2937881, 0.99412735, 0.02930586, 0.02384863, 0.43375763};
        double[] array2 = {0.55389835, 0.51201537, 0.65602595, 0.06291119, 0.19727478, 0.51673646,
                0.01277999, 0.00701273, 0.33661381, 0.56618559, 0.6419008, 0.33657229};
        Matrix matrix1 = new Matrix(array1, 6, 1);
        Matrix matrix2 = new Matrix(array2, 2, 6);
        Matrix resultMatrix = new Matrix(1, 2);
        Tensor tensor = new Tensor(resultMatrix);
        tensor.addmm(matrix1.transpose(), matrix2.transpose());
        assertArrayEquals(new double[]{1.4445469998540097, 0.5240780493796711}, tensor.getDenseArray(), delta);
    }
}
