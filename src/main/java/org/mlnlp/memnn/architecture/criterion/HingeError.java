package org.mlnlp.memnn.architecture.criterion;

import org.mlnlp.memnn.tensor.Tensor;

/**
 * Created by qingqingcai on 9/3/15.
 */
public class HingeError extends Criterion {
    Tensor inpuT = gradInput.asTensor().tensor;
    Tensor targetT = gradInput.asTensor().tensor;

}
