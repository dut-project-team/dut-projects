package com.blogspot.sontx.whitelight.net;

import com.blogspot.sontx.whitelight.bean.UserConfig;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public class RequestEditUserConfigRequestPackage extends RequestPackage {
    private byte mLightId;
    private UserConfig mUserConfig;

    protected RequestEditUserConfigRequestPackage() {
        super(COMMAND_EDIT_USERCONFIG);
    }


    public byte getLightId() {
        return mLightId;
    }

    public void setLightId(byte mLightId) {
        this.mLightId = mLightId;
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
        byte[] buff = new byte[data.length + 2];
        buff[0] = mCommand;
        buff[1] = mLightId;
        System.arraycopy(data, 0, buff, 2, data.length);
        return buff;
    }
}
