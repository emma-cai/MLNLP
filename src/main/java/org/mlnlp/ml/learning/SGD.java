package org.mlnlp.ml.learning;

import edu.stanford.nlp.optimization.DiffFunction;
import edu.stanford.nlp.optimization.SGDMinimizer;

/**
 * Created by qingqingcai on 8/24/15.
 */
public class SGD {

    public static void main(String[] args) {
        final int dim = 50000;
        final double maxVar = 5;
        final double[] var = new double[dim];
        double[] init = new double[dim];

        for (int i = 0; i < dim; i++) {
            init[i] = (Math.random() - 0.5);
            var[i] = maxVar * (i + 1) / dim;
        }

        final double[] grads = new double[dim];

        final DiffFunction f = new DiffFunction() {
            @Override
            public double[] derivativeAt(double[] x) {
                double val = Math.PI * valuePow(x, Math.PI - 1);
                for (int i = 0; i < dim; i++) {
                    grads[i] = x[i] * var[i] * val;
                }
                return grads;
            }

            @Override
            public double valueAt(double[] x) {
                return 1.0 + valuePow(x, Math.PI);
            }

            @Override
            public int domainDimension() {
                return dim;
            }

            private double valuePow(double[] x, double pow) {
                double val = 0.0;
                for (int i = 0; i < dim; i++) {
                    val += x[i] * x[i] * var[i];
                }
                return Math.pow(val * 0.5, pow);
            }


        };

        SGDMinimizer min = new SGDMinimizer(0D, 50);
        double[] result = min.minimize(f, 1.0E-4, init);
        for (int i = 0; i < result.length; i++) {
            System.out.println("i = " + result[i]);
        }
    }

}
