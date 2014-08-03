package epamlabs.task1;

import static java.lang.Math.*;
/**
 * Created by Adevi on 8/3/2014.
 */
public class Sincos implements MathFunction {
    @Override
    public double apply(double arg) {
        return sin(arg) * cos(arg);
    }
}
