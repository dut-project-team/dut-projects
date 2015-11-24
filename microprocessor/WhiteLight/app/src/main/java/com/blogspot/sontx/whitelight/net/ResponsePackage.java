package com.blogspot.sontx.whitelight.net;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public abstract class ResponsePackage {
    private boolean mIsSuccess;

    public void init(byte[] data) {
        mIsSuccess = data[0] != 0;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }
}
