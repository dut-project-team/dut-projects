package com.blogspot.sontx.whitelight.bean;

import java.nio.charset.Charset;

/**
 * Copyright by NE 2015.
 * Created by noem on 11/11/2015.
 */
public class UserConfig extends Config {
    public static final int CONFIG_LIGHT_OFF = 0;
    public static final int CONFIG_LIGHT_ON = 1;
    public static final int CONFIG_TYPE_USER = 0;
    public static final int CONFIG_TYPE_DEF = -2;
    public static final int CONFIG_TYPE_OFF = -1;
    public static final int MAX_LEN_NAME = 2;
    public static final int MAX_NUM_CONFIG = 5;
    private byte sensor;
    private byte extra;
    private byte nConfig;
    private short[] lstTime = new short[MAX_NUM_CONFIG];
    private byte[] lstConfig = new byte[MAX_NUM_CONFIG];
    private String name;

    public static int getFrameSize() {
        return MAX_LEN_NAME + MAX_NUM_CONFIG * 3 + 3;
    }

    public static UserConfig parse(byte[] frame, int offset, int length) {
        if (length != getFrameSize() || offset < 0 || offset + length > frame.length)
            return null;
        UserConfig config = new UserConfig();

        config.sensor = frame[offset + 0];

        config.extra = frame[offset + 1];

        int _offset = 2;

        config.lstTime = new short[MAX_NUM_CONFIG];
        for (int i = 0; i <= MAX_NUM_CONFIG; i += 2) {
            short time = bytesToInt16(frame[offset + _offset + i], frame[offset + _offset + i + 1]);
            config.lstTime[i / 2] = time;
        }
        _offset += MAX_NUM_CONFIG * 2;

        config.lstConfig = new byte[MAX_NUM_CONFIG];
        System.arraycopy(frame, offset + _offset, config.lstConfig, 0, MAX_NUM_CONFIG);
        _offset += MAX_NUM_CONFIG;

        config.nConfig = frame[offset + _offset];
        _offset++;

        byte[] name = new byte[MAX_LEN_NAME];
        System.arraycopy(frame, offset + _offset, name, 0, MAX_LEN_NAME);
        config.name = new String(name, Charset.forName("ASCII"));

        return config;
    }

    public static String shortToTimeString(short time) {
        if (time == 1440)
            return "00:00";
        return String.format("%02d:%02d", time / 60, time % 60);
    }

    private static byte[] getFixedBytes(String st, int count) {
        byte[] buff = new byte[count];
        byte[] st_buff = st.getBytes(Charset.forName("ASCII"));
        System.arraycopy(st_buff, 0, buff, 0, Math.min(buff.length, st_buff.length));
        return buff;
    }

    public static byte combine(byte low, byte high) {
        return (byte) ((low & 0x0F) | (high << 4));
    }

    public byte getSensor() {
        return sensor;
    }

    public void setSensor(byte sensor) {
        this.sensor = sensor;
    }

    public byte getExtra() {
        return extra;
    }

    public void setExtra(byte extra) {
        this.extra = extra;
    }

    public short[] getLstTime() {
        return lstTime;
    }

    public void setLstTime(short[] lstTime) {
        this.lstTime = lstTime;
    }

    public byte[] getLstConfig() {
        return lstConfig;
    }

    public void setLstConfig(byte[] lstConfig) {
        this.lstConfig = lstConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getNConfig() {
        return nConfig;
    }

    public void setNConfig(byte nConfig) {
        this.nConfig = nConfig;
    }

    public byte getLightSensor() {
        return (byte) (sensor & (byte) 0x0F);
    }

    public byte getPeopleSensor() {
        return (byte) ((sensor & 0xF0) >> 4);
    }

    public byte getLightPin() {
        return (byte) ((extra & 0xF0) >> 4);
    }

    public byte getLightType() {
        return (byte) (extra & (byte) 0x0F);
    }

    /**
     * Value type of config must sync with Config.h in server project
     *
     * @return type of config
     */
    public int getConfigType() {
        if (nConfig >= 0)
            return CONFIG_TYPE_USER;
        else if (nConfig == -1)
            return CONFIG_TYPE_OFF;
        return CONFIG_TYPE_DEF;// -2
    }

    @Override
    public byte[] getBytes() {
        int length = getFrameSize();
        byte[] buff = new byte[length];
        int offset = 0;

        buff[0] = sensor;

        buff[1] = extra;
        offset += 2;

        byte[] times = new byte[MAX_NUM_CONFIG * 2];
        for (int i = 0; i <= MAX_LEN_NAME; i += 2) {
            byte[] bytes = int16ToBytes(lstTime[i / 2]);
            times[i] = bytes[0];
            times[i + 1] = bytes[1];
        }
        System.arraycopy(times, 0, buff, offset, MAX_NUM_CONFIG * 2);
        offset += MAX_NUM_CONFIG * 2;

        System.arraycopy(lstConfig, 0, buff, offset, MAX_NUM_CONFIG);
        offset += MAX_NUM_CONFIG;

        buff[offset] = nConfig;
        offset++;

        System.arraycopy(getFixedBytes(name, MAX_LEN_NAME), 0, buff, offset, MAX_LEN_NAME);

        return buff;
    }
}
