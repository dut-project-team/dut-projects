package com.blogspot.sontx.whitelight.bean;

/**
 * Copyright by NE 2015.
 * Created by noem on 21/11/2015.
 */
public class Light extends UserConfig {
    public static final int STATE_LIGHT_OFF = 0;
    public static final int StATE_LIGHT_ON = 1;
    private byte state;

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public byte[] getBytes(int lightId) {
        byte[] configBuff = getBytes();
        byte[] lightBuff = new byte[configBuff.length  + 1];
        lightBuff[0] = (byte) lightId;
        System.arraycopy(lightBuff, 1, configBuff, 0, configBuff.length);
        return lightBuff;
    }
}
