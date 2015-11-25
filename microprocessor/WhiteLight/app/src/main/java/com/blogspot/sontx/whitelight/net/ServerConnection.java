package com.blogspot.sontx.whitelight.net;

import android.os.Handler;

import com.blogspot.sontx.libex.net.MixSocket;
import com.blogspot.sontx.whitelight.bean.DefConfig;
import com.blogspot.sontx.whitelight.bean.Light;
import com.blogspot.sontx.whitelight.bean.UserConfig;

import java.io.IOException;
import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 21/11/2015.
 */
public final class ServerConnection {
    private static ServerConnection instance = null;
    public static ServerConnection getInstance() {
        if (instance == null)
            instance = new ServerConnection();
        return instance;
    }

    private OnRefreshDataListener mOnRefreshDataListener;
    private RefreshObject refreshObject = new RefreshObject();

    private Handler handler = new Handler();

    public void setOnRefreshDataListener(OnRefreshDataListener listener) {
        mOnRefreshDataListener = listener;
    }

    private MixSocket socket = null;

    public void setRefreshRate(int rate) {
        handler.postDelayed(refreshObject, rate);
    }

    public void connect(String ip, int port) throws IOException {
        disconnect();
        socket = new MixSocket(ip, port);
    }

    public void disconnect() {
        if (socket == null)
            return;
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Light> getAllLights() {
        return null;
    }

    public boolean updateUserConfig(UserConfig config, int id) {
        return true;
    }

    public boolean updateDefConfig(DefConfig config, int id) {
        return true;
    }

    public boolean addLight(UserConfig light) {
        return true;
    }

    public boolean removeLight(int id) {
        return true;
    }

    private class RefreshObject implements Runnable {
        @Override
        public void run() {
            // update
        }
    }

    public interface OnRefreshDataListener {
        // refresh light state only(off/on)
        void refreshLightStates(byte[] lightStates);
    }
}
