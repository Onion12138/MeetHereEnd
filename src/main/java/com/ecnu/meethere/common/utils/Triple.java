package com.ecnu.meethere.common.utils;

public class Triple<F, S, T> {
    private final F first;

    private final S second;

    private final T thrid;

    public Triple(F first, S second, T thrid) {
        this.first = first;
        this.second = second;
        this.thrid = thrid;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThrid() {
        return thrid;
    }
}
