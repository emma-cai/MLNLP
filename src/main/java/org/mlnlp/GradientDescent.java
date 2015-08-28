package org.mlnlp;

/**
 * Created by qingqingcai on 8/27/15.
 */
public class GradientDescent {

    public static void main(String[] args) {
        double x_old = 0;
        double x_new = 6;
        double eps = 0.01;
        double precision = 0.00001;

        while (Math.abs(x_new - x_old) > precision) {
            x_old = x_new;
            x_new = x_old - eps * computeFunctionDerivative(x_old);
            System.out.println("Local minimum occurs at " + x_new + "\t\t\tf(x) = " + computeFunction(x_new));
        }
    }

    private static double computeFunction(double x) {
        return Math.pow(x, 4) - 3.0 * Math.pow(x, 3) + 2.0;
    }

    private static double computeFunctionDerivative(double x) {
        return 4.0 * Math.pow(x, 3) - 9.0 * Math.pow(x, 2);
    }
}
