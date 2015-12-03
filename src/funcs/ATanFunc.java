package funcs;

import com.syouth.parser.Function;

/**
 * Created by syouth on 11.11.15.
 */
public class ATanFunc implements Function<Double, Double> {
    @Override
    public Double apply(Double arg) {
        return Math.toDegrees(Math.atan(arg));
    }
}
