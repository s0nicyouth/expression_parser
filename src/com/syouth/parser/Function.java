package com.syouth.parser;

/**
 * Created by syouth on 11.11.15.
 */
public interface Function<ARG, RET> {
    RET apply(ARG arg);
}
