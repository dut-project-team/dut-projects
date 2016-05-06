package com.blogspot.sontx.dut.soccer.bean;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/04/2016.
 */
public class City {
    private int cityId;
    private String name;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
