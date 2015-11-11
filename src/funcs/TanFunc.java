package funcs;

import com.syouth.parser.Function;

/**
 * Created by syouth on 11.11.15.
 */
public class TanFunc implements Function<Double, Double> {
    @Override
    public Double apply(Double arg) {
        return Math.tan(Math.toRadians(arg));
    }
}
