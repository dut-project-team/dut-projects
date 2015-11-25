package com.blogspot.sontx.whitelight.net;

import android.os.Handler;

import com.blogspot.sontx.libex.net.MixSocket;
import com.blogspot.sontx.libex.util.Convert;
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
    private int refreshRate;

    private Handler handler = new Handler();
    private MixSocket socket = null;

    public void setOnRefreshDataListener(OnRefreshDataListener listener) {
        mOnRefreshDataListener = listener;
    }

    public void setRefreshRate(int rate) {
        refreshRate = rate;
        if (socket != null && !socket.isClosed())
            handler.postDelayed(refreshObject, rate);
    }

    public void connect(String ip, int port) throws IOException {
        disconnect();
        socket = new MixSocket(ip, port);
        setRefreshRate(refreshRate);
    }

    public void disconnect() {
        if (socket == null)
            return;
        try {
            handler.removeCallbacks(refreshObject);
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Light> getAllLights() {
        return null;
    }

    public List<DefConfig> getAllDefConfigs() {
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

    private boolean checkState() {
        return socket == null || socket.isClosed();
    }

    private byte[] getLightStates() {
        if (!checkState())
            return null;
        RequestGetLightStateRequestPackage request = new RequestGetLightStateRequestPackage();
        boolean ok = socket.send(Convert.base64Encode(request.getBytes()));
        if (ok) {
            String receive = socket.receiveString();
            if (receive.length() > 0) {
                byte[] buff = Convert.base64Decode(receive);
                ResponseLightStatePackage response = new ResponseLightStatePackage();
                response.init(buff);
                byte[] states = response.getStates();
                return states;
            }
        }
        return null;
    }

    private class RefreshObject implements Runnable {
        @Override
        public void run() {
            if (mOnRefreshDataListener != null) {
                byte[] states = getLightStates();
                mOnRefreshDataListener.refreshLightStates(states);
            }
        }
    }

    public interface OnRefreshDataListener {
        // refresh light state only(off/on)
        void refreshLightStates(byte[] lightStates);
    }
}
