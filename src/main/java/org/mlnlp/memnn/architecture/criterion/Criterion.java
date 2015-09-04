package org.mlnlp.memnn.architecture.criterion;

import org.mlnlp.memnn.tensor.TensorDatum;

/**
 * Created by qingqingcai on 9/3/15.
 */
public class Criterion {

    public double output;
    public TensorDatum gradInput;

    public Criterion() {
        output = 0.0;
        gradInput = new TensorDatum();
    }
}
