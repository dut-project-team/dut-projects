package com.blogspot.sontx.dut.soccer.bo;

import com.blogspot.sontx.dut.soccer.bean.Money;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright NoEm 2016
 * Created by Noem on 6/5/2016.
 */
public final class SampleData {
    private SampleData() {}

    public static List<Integer> getSlots() {
        List<Integer> slots = new ArrayList<>();
        slots.add(5);
        slots.add(7);
        slots.add(11);
        return slots;
    }

    public static List<Money> getMoney() {
        List<Money> money = new ArrayList<>();
        money.add(new Money(36000));
        money.add(new Money(35000));
        money.add(new Money(32000));
        return money;
    }
}
