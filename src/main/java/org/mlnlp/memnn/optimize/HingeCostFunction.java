package org.mlnlp.memnn.optimize;

import org.mlnlp.memnn.architecture.criterion.Criterion;
import org.mlnlp.memnn.tensor.Datum;
import org.mlnlp.memnn.tensor.Matrix;
import org.mlnlp.memnn.tensor.ModuleParams;
import org.mlnlp.memnn.tensor.Tensor;
import org.mlnlp.memnn.utils.MemNNUtils;

/**
 * Created by qingqingcai on 9/3/15.
 */
public class HingeCostFunction extends CostFunction {

    public Criterion objective;

    @Override
    public double func(ModuleParams params, Datum input, Datum target) {

        Tensor inputT = input.asTensor().tensor();
        Tensor targetT = target.asTensor().tensor();
        Tensor paramsT = params.weight;
        Matrix inputM = new Matrix(inputT.values, inputT.shape[0], inputT.shape[1]);
        Matrix targetM = new Matrix(targetT.values, targetT.shape[0], targetT.shape[1]);
        Matrix paramM = new Matrix(paramsT.values, paramsT.shape[0], paramsT.shape[1]);
        double m = MemNNUtils.computeSO(inputM, paramM, targetM);
        return 0;
    }
}
