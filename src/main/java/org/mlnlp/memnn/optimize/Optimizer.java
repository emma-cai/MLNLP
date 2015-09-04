package org.mlnlp.memnn.optimize;

import org.mlnlp.memnn.tensor.ModuleParams;

import java.util.List;

/**
 * Optimization algorithm.
 *
 * @author jamesgung
 */
public abstract class Optimizer {

    public double cost; // f(x) evaluated before update

    public abstract void optim(CostFunction costFunction, ModuleParams parameters, List<Instance> batch);

}
