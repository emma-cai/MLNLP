package org.mlnlp.memnn.utils;

import org.mlnlp.memnn.tensor.Matrix;
import org.mlnlp.memnn.tensor.Tensor;

/**
 * Created by qingqingcai on 9/3/15.
 */
public class MemNNUtils {

    public static double computeSO(Matrix X, Matrix U, Matrix Y) {

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
