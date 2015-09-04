package org.mlnlp.memnn.tensor;


/**
 * Learned parameters and gradients w.r.t. these parameters.
 *
 * @author jamesgung
 */
public class ModuleParams {

    public Tensor weight;
    public Tensor bias;
    public Tensor gradWeight;
    public Tensor gradBias;

    public ModuleParams(Tensor weight, Tensor bias, Tensor gradWeight, Tensor gradBias) {
        this.weight = weight;
        this.bias = bias;
        this.gradWeight = gradWeight;
        this.gradBias = gradBias;
    }

    public ModuleParams(ModuleParams copy) {
        this.weight = new Tensor(copy.weight);
        this.bias = new Tensor(copy.bias);
        this.gradWeight = new Tensor(copy.gradWeight);
        this.gradBias = new Tensor(copy.gradBias);
    }

    public void zeroGradParameters() {
        gradWeight.zero();
        gradBias.zero();
    }

    public void addL2Penalty(double coefficient, boolean regularizeBias) {
        this.gradWeight.add(weight, coefficient);
        if (regularizeBias) {
            this.gradBias.add(bias, coefficient);
        }
    }


}
