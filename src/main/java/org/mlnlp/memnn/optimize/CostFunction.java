package org.mlnlp.memnn.optimize;

import org.mlnlp.memnn.tensor.Datum;
import org.mlnlp.memnn.tensor.ModuleParams;


/**
 * Function class. f(x), df/dx = func(x)
 *
 * @author jamesgung
 */
public abstract class CostFunction {

    public double cost; // f(x)

    public abstract double func(ModuleParams params, Datum input, Datum target);

}
