package org.mlnlp.nlp.MemNNs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by qingqingcai on 9/2/15.
 */
public class PseudoCode {

    // n: vocabulary_size
    // D: dimension_size
    int D = 50; // initialized to be 50
    double[] vocabulary;
    Map<String, Long> H;
    //TODO: What is V
    //???
    List<double[]> V;

    // list of instances
    List<Instance> train_lines;
    List<Instance> test_lines;

    // list of I(x)
    double[][] L_train = new double[train_lines.size()][vocabulary.length];
    double[][] L_test = new double[test_lines.size()][vocabulary.length];

    public void load() {

        // (a) initialize vocabulary: words appear in both training and testing data

        // (b) initialize H: a map from lexical to its ID

        // TODO
        // (c) initialize V: ???

        // (d)
    }
    public void train(int n_epochs) {

        // train_model is initialized to be zero
        TrainModel train_model = null;

        int lenW = vocabulary.length;
        int W = 3 * lenW + 3;

        // randomly initialize U_Ot, values are randomly selected between -0.1 to 0.1;
        double[][] U_Ot = new double[D][W];

        // randomly initialize U_R, values are randomly selected between -0.1 and 0.1
        double[][] U_R = new double[D][W];

        double prev_err = 0D;
        for (int epoch = 0; epoch < n_epochs; epoch++) {
            double total_error = 0D;
            int n_wrong = 0;

            for (int i = 0; i < train_lines.size(); i++) {
                Instance line = train_lines.get(i);
                if ("q".equals(line.type)) {    // indicates question
                    List<Integer> refs = line.refs; // Indexing from 1
                    List<Integer> f = refs.stream()
                            .map(ref -> ref-1)
                            .collect(Collectors.toList());  // Indexing from 0
                    int id = line.ID - 1;   // Indexing from 0

                    // all indices from
                    List<Integer> indices = range(i-id, i+1);
                    List<double[]> memory_list = indices.stream()
                            .map(idx -> L_train[idx])
                            .collect(Collectors.toList());


                    if (train_model == null) {
                        train_model = new TrainModel(lenW, f.size());
                    }

                    List<Integer> m = f;
                    // TODO
                    List<Integer> mm = new ArrayList<>(); //TODO
                    for (int j = 0; j < f.size(); j++) {
                        mm.add(O_t(
                                newM,
                                memory_list
                        ));
                    }

                    double err = train_model(H.get("answer"),
                            gamma, memory_list, V, id, ???)[0];
                    total_error += err;
                    System.out.println("epoch: " + epoch + "\terr: " + (total_error / train_lines.size()));
                }
            }
        }
    }





    // some utils

    // Find the index which s_Ot > 0
    public static int O_t(List<Integer> xs,
                           List<double[]> L,
                           TrainModel train_model) {
        int t = 0;
        for (int i = 0; i < L.size()-1; i++) {
            // Last element is the answer, so we can ignore that.
            if (train_model.s_Ot(xs, i, t, L) > 0) {
                t = i;
            }
        }
        return t;
    }
}

class Instance {
    int ID;         // Indexing from 1
    String text;
    String type;
    List<Integer> refs;     // list of reference's ID, indexing from 1 (in training/testing text files)

}

class TrainModel {

    private double[] phi_x1(int x_t, List<double[]> L) {
        return [L[x_t], 0 ... 0, 0 ... 0, 0, 0, 0];
    }

    private double[] phi_x2(int x_t, List<double[]> L) {
        return [0 ... 0, L[x_t], 0 ... 0, 0, 0, 0];
    }

    private double[] phi_y(int x_t, List<double[]> L) {
        return [0 ... 0, 0 ... 0, L[x_t], 0, 0, 0];
    }

    private double[] phi_t(int x_t, int y_t, List<double[]> L) {
        return [0...0, 0...0, 0...0, ????];
    }

    // TODO
    public double s_Ot(List<Integer> xs, int y_t, int yp_t, List<double[]> L) {
        double[] result = new double[xs.size()];
        double[] XPhi;
        for (int t = 0; t < xs.size(); t++) {
            x_t = xs.get(t);
            if (t == 0) {
                XPhi = phi_x1(x_t, L);
            } else {
                XPhi = phi_x2(x_t, L);
            }
            double[] YPhi = phi_y(y_t, L) - phi_y(yp_t, L) + phi_t(x_t, y_t, yp_t, L);
            result[t] = dot(dot(XPhi, U_Ot.T),
                    dot(U_Ot, YPhi));
        }
        return result.sum();
    }

    public double sR(List<Integer> xs, int y_t, List<double[]> L, List<double[]> V) {
        double[] result = new double[xs.size()];
        double[] XPhi;
        for (int t = 0; t < xs.size(); t++) {
            x_t = xs.get(t);
            if (t == 0) {
                XPhi = phi_x1(x_t, L);
            } else {
                XPhi = phi_x2(x_t, L);
            }
            result[t] = dot(dot(XPhi, U_R.T),
                    dot(U_R, phi_y(y_t, V)));
        }
        return result.sum();
    }
    public TrainModel(int lenW, int f_len) {

    }
}
