package com.blogspot.sontx.whitelight.net;

import com.blogspot.sontx.whitelight.bean.UserConfig;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public class RequestAddLightRequestPackage extends RequestPackage {
    private UserConfig mUserConfig;

    public RequestAddLightRequestPackage() {
        super(COMMAND_ADD_LIGHT);
    }

    public UserConfig getUserConfig() {
        return mUserConfig;
    }

    public void setUserConfig(UserConfig mUserConfig) {
        this.mUserConfig = mUserConfig;
    }

    @Override
    public byte[] getBytes() {
        byte[] data = mUserConfig.getBytes();
        byte[] buff = new byte[data.length + 1];
        buff[0] = mCommand;
        System.arraycopy(data, 0, buff, 1, data.length);
        return buff;
    }
}
