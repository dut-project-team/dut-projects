package com.blogspot.sontx.whitelight.net;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public abstract class RequestPackage {
    public static final byte COMMAND_EDIT_USERCONFIG = 1;
    public static final byte COMMAND_ADD_LIGHT = 2;
    public static final byte COMMAND_REMOVE_LIGHT = 3;
    public static final byte COMMAND_GET_USERCONFIG = 4;
    public static final byte COMMAND_GET_DEFCONFIG = 5;
    public static final byte COMMAND_GET_LIGHTSTATE = 6;
    public static final byte COMMAND_GET_AVAILABLE_CONFIG = 7;
    public static final byte COMMAND_EDIT_DEFCONFIG = 8;
    public static final byte COMMAND_UPDATE_TIME = 9;

    protected byte mCommand;

    protected RequestPackage(byte command) {
        mCommand = command;
    }

    public byte getCommand() {
        return mCommand;
    }

    public void setCommand(byte command) {
        mCommand = command;
    }

    public byte[] getBytes() {
        byte[] buff = new byte[1];
        buff[0] = mCommand;
        return buff;
    }
}
