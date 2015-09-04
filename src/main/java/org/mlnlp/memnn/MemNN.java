package org.mlnlp.memnn;

import org.apache.commons.lang3.StringUtils;
import org.mlnlp.memnn.data.TrainingDataMatrix;
import org.mlnlp.memnn.tensor.Matrix;
import org.mlnlp.memnn.tensor.Tensor;

/**
 * Created by qingqingcai on 9/3/15.
 */
public class MemNN {

    private final static int n = 2;
    private final static int D = 6;

    public static void main(String[] args) {


        String filename = "/Users/qingqingcai/Desktop/simpledata/en-10k/qa1_single-supporting-fact_train.txt";

        TrainingDataMatrix tdm = new TrainingDataMatrix();
        tdm.load(filename);

        for (int i = 0; i < tdm.matrix.length; i++) {
            int id = tdm.trainingSampleIndexer.inverse().get(i);
            String sentence = tdm.trainingData.stream().filter(t -> t.id == id).map(x -> x.sentence).findFirst().orElse(null);
            System.out.print(sentence + StringUtils.SPACE);
            for (int j = 0; j < tdm.matrix[i].length; j++) {
                System.out.print(tdm.matrix[i][j] + StringUtils.SPACE);
            }
            System.out.println(StringUtils.EMPTY);
        }



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

