package org.mlnlp.memnn.optimize;


import org.mlnlp.memnn.data.MemnetTuple;
import org.mlnlp.memnn.tensor.ModuleParams;

import java.util.List;

/**
 * Stochastic gradient descent implementation.
 *
 * @author jamesgung
 */
public class StochasticGradientDescent extends Optimizer {

    protected ModuleParams savedParams = null;
    public double clr;

    public double learningRate = 1e-3;
    public double learningRateDecay = 0;
    public double weightDecay = 0;
    public double momentum = 0.1;
    public double dampening = 0;
    public boolean nesterov = true;
    public int numEvals = 0;

    public StochasticGradientDescent(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Configuration setup constructor.
     *
     * @param learningRate      learning rate
     * @param learningRateDecay learning rate decay parameter
     * @param weightDecay       weight decay parameter (l2 regularization)
     * @param momentum          momentum
     * @param dampening         dampening factor on momentum
     * @param nesterov          use nesterov momentum or not
     */
    public StochasticGradientDescent(double learningRate, double learningRateDecay, double weightDecay, double momentum,
                                     double dampening, boolean nesterov) {
        this.learningRate = learningRate;
        this.learningRateDecay = learningRateDecay;
        this.weightDecay = weightDecay;
        this.momentum = momentum;
        this.dampening = dampening;
        this.nesterov = nesterov;
        this.numEvals = 0;
        if (nesterov && (dampening != 0 || momentum <= 0)) {
            throw new IllegalArgumentException("Nesterov momentum requires 0 dampening and positive momentum.");
        }
    }

    /**
     * Perform optimization step on a batch.
     *
     * @param costFunction objective function
     * @param parameters   model parameters (flattened)
     * @param instances    batch instances
     */
    public void optim(CostFunction costFunction, ModuleParams parameters, List<MemnetTuple> instances) {

        // evaluate f(x) and df/dx
        double cost = 0;

        for (MemnetTuple instance : instances) {
            cost += costFunction.func(parameters, instance.input, instance.expected);
        }

        cost /= instances.size();

        // apply learning rate decay
        if (weightDecay != 0) {
            parameters.gradWeight.add(parameters.weight, weightDecay);
            parameters.gradBias.add(parameters.bias, weightDecay);
        }

        // apply momentum
        if (momentum != 0) {
            if (savedParams == null) {
                savedParams = new ModuleParams(parameters);
            } else {
                savedParams.gradWeight.mult(momentum);
                savedParams.gradWeight.add(parameters.gradWeight, 1.0 - dampening);
                savedParams.gradBias.mult(momentum);
                savedParams.gradBias.add(parameters.gradBias, 1.0 - dampening);
            }
            if (nesterov) {
                parameters.gradWeight.add(savedParams.gradWeight, momentum);
                parameters.gradBias.add(savedParams.gradBias, momentum);
            } else {
                parameters.gradWeight.copy(savedParams.gradWeight);
                parameters.gradBias.copy(savedParams.gradBias);
            }
        }

        // learning rate decay (annealing)
        clr = learningRate / (1.0 + numEvals * learningRateDecay);

        parameters.weight.add(parameters.gradWeight, -clr / instances.size());
        parameters.bias.add(parameters.gradBias, -clr / instances.size());

        // update evaluation counter
        ++numEvals;
        this.cost = cost;
    }
}
