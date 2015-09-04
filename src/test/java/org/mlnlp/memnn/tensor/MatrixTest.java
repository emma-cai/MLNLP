package org.mlnlp.memnn.tensor;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by qingqingcai on 9/3/15.
 */
public class MatrixTest {

    private static final double delta = 1e-10;
    private final static int n = 2;
    private final static int D = 6;

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

    @Test
    public void testAddmm2() {

        // PhiX
        double[] PhiXArray = {0.74248796, 0.2937881, 0.99412735, 0.02930586, 0.02384863, 0.43375763};
        Matrix PhiX = new Matrix(PhiXArray, D, 1);

        // PhiY
        double[] PhiYArray = {1, 1, 0, 0, 0, 0, 1};
        Matrix PhiY = new Matrix(PhiYArray, D, 1);

        // U_Ot
        Random random = new Random(123);
        Matrix U_Ot = new Matrix(n, D);
        U_Ot.initRandomUniform(-0.1, 0.1, random);

        double score = s_Ot(PhiX, U_Ot, PhiY);
        assertEquals(0.00519529312081371, score, delta);

    }

    private double s_Ot(Matrix X, Matrix U, Matrix Y) {

        Tensor tensor1 = new Tensor(new Matrix(X.shape[1], U.shape[0]));
        Tensor tensor2 = new Tensor(new Matrix(U.shape[0], Y.shape[1]));
        Tensor finalTensor = new Tensor(new Matrix(1, 1));
        tensor1.addmm(X.transpose(), U.transpose());
        tensor2.addmm(U, Y);
        finalTensor.addmm(tensor1, tensor2);
        return finalTensor.values[0];
    }
}
