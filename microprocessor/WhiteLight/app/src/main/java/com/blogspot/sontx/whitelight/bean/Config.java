package com.blogspot.sontx.whitelight.bean;

/**
 * Copyright by NE 2015.
 * Created by noem on 11/11/2015.
 */
public abstract class Config {
    protected static byte[] int16ToBytes(short int16) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (int16 >> 8);
        bytes[1] = (byte) (int16 & 0x00ff);
        return bytes;
    }

    protected static short bytesToInt16(byte b1, byte b2) {
        return (short) (((short) b1) << 8 | (short) b2);
    }

    protected static byte[] int32ToBytes(int int32) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (int32 >> 24);
        bytes[1] = (byte) (int32 >> 16);
        bytes[2] = (byte) (int32 >> 8);
        bytes[3] = (byte) int32;
        return bytes;
    }

    protected static int bytesToInt32(int b1, int b2, int b3, int b4) {
        return b1 << 24 | b2 << 16 | b3 << 8 | b4;
    }

    public abstract byte[] getBytes();
}
