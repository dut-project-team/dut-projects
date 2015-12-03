package com.blogspot.sontx.whitelight.bean;

import com.blogspot.sontx.libex.util.Convert;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 * Note: state must have 4 configs(include time), if you only want to use
 * some config(ex 1, 2 or 3) then you have to set unused config to zero
 */
public class DefConfig extends Config {
    public static final int CONFIG_LIGHTSENSOR_EVERYTIME = 0x01;
    public static final int CONFIG_LIGHTSENSOR_NIGHT = 0x02;
    public static final int CONFIG_PEOPLESENSOR_EVERYTIME = 0x03;
    public static final int CONFIG_PEOPLESENSOR_NIGHT = 0x04;
    public static final int CONFIG_BOTHSENSOR_EVERYTIME = 0x05;
    public static final int CONFIG_BOTHSENSOR_NIGHT = 0x06;

    private byte lightThreshold;
    private int state;

    public static int getFrameSize() {
        return 1 + 4;
    }

    public static DefConfig parse(byte[] frame, int offset, int length) {
        if (length != getFrameSize() || offset < 0 || offset + length > frame.length)
            return null;
        DefConfig config = new DefConfig();
        config.lightThreshold = frame[offset + 0];
        config.state = Convert.bytesToInteger(
                new byte[] {frame[offset + 4], frame[offset + 3], frame[offset + 2], frame[offset + 1]});
        return config;
    }

    public Level[] exportState() {
        //int _state = Convert.endianSwap(state);
        int num = ((state >> 30) & 0x00000003) + 1;// 00 to 11, save max is 4 values
        Level[] levels = new Level[num];
        int offset = 2;
        for(int i = 0; i < num; i++) {
            int value;
            boolean isTime;
            offset++;
            isTime = ((state >> (32 - offset)) & 0x00000001) != 0;
            if(isTime) {
                offset += 11;
                value = (state >> (32 - offset)) & 0x000007FF;
            } else {
                offset += 4;
                value = (state >> (32 - offset)) & 0x0000000F;
            }
            Level level = new Level(value, isTime);
            levels[i] = level;
        }
        return levels;

    }

    public void importState(Level[] levels) {
        state = 4 - 1;// count of setting level, hardcode :|
        state <<= 30;
        int offset = 30;
        for(int i = 0; i < 4; i++) {
            Level level = levels[i];
            if(level.isTime) {
                offset -= 12;
                state |= (level.value | 0x800) << offset;
            } else {
                offset -= 5;
                state |= level.value << offset;
            }
        }
    }

    @Override
    public byte[] getBytes() {
        byte[] buff = new byte[getFrameSize()];
        buff[0] = lightThreshold;
        byte[] holder = Convert.integerToBytes(state);
        for (int i = 0, j = holder.length - 1; i < holder.length / 2; i++, j--) {
            byte temp = holder[i];
            holder[i] = holder[j];
            holder[j] = temp;
        }
        System.arraycopy(holder, 0, buff, 1, 4);
        return buff;
    }

    public byte[] getBytes(int defconfigId) {
        byte[] configBuff = getBytes();
        byte[] buff = new byte[configBuff.length  + 1];
        buff[0] = (byte) defconfigId;
        System.arraycopy(configBuff, 0, buff, 1, configBuff.length);
        return buff;
    }

    public byte getLightThreshold() {
        return lightThreshold;
    }

    public void setLightThreshold(byte lightThreshold) {
        this.lightThreshold = lightThreshold;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static class Level {
        public final int value;
        public final boolean isTime;
        public Level(int value, boolean isTime) {
            this.value = value;
            this.isTime = isTime;
        }
    }
}
