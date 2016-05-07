package com.blogspot.sontx.dut.soccer.bean;

/**
 * Copyright NoEm 2016
 * Created by Noem on 6/5/2016.
 */
public class Money {
    private int value;

    public Money() {}

    public Money(int value) { this.value = value; }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%d VND", value);
    }
}
