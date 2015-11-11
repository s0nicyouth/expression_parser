package com.syouth.parser;

/**
 * Created by syouth on 11.11.15.
 */
public class FunctionDescription<ARG, RET> {
    private String mFunctionName;
    private Function<ARG, RET> mEvaluator;

    public FunctionDescription(String mFunctionName, Function<ARG, RET> mEvaluator) {
        this.mFunctionName = mFunctionName;
        this.mEvaluator = mEvaluator;
    }

    public String getmFunctionName() {
        return mFunctionName;
    }

    public Function<ARG, RET> getmEvaluator() {
        return mEvaluator;
    }
}
