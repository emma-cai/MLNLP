package org.mlnlp.memnn;

import org.mlnlp.memnn.tensor.Matrix;
import org.mlnlp.memnn.tensor.Tensor;

import java.util.Random;

/**
 * Created by qingqingcai on 9/3/15.
 */
public class MemNN {

    private final static int n = 2;
    private final static int D = 6;

    public static void main(String[] args) {

        // PhiX
        double[] PhiXArray = {0.74248796, 0.2937881, 0.99412735, 0.02930586, 0.02384863, 0.43375763};
        Matrix PhiX = new Matrix(PhiXArray, D, 1);
        System.out.println(PhiX);

        // PhiY
        double[] PhiYArray = {1, 1, 0, 0, 0, 0, 1};
        Matrix PhiY = new Matrix(PhiYArray, D, 1);
        System.out.println(PhiY);

        // U_Ot
        Random random = new Random(123);
        Matrix U_Ot = new Matrix(n, D);
        U_Ot.initRandomUniform(-0.1, 0.1, random);
        System.out.println(U_Ot.toString());


        double score = s_Ot(PhiX, U_Ot, PhiY);
        System.out.println("score = " + score);

    }

    public static double s_Ot(Matrix X, Matrix U, Matrix Y) {

        Tensor tensor1 = new Tensor(new Matrix(X.shape[1], U.shape[0]));
        Tensor tensor2 = new Tensor(new Matrix(U.shape[0], Y.shape[1]));
        Tensor finalTensor = new Tensor(new Matrix(1, 1));
        tensor1.addmm(X.transpose(), U.transpose());
        tensor2.addmm(U, Y);
        finalTensor.addmm(tensor1, tensor2);
        System.out.println(finalTensor);
        return finalTensor.values[0];
    }
}

