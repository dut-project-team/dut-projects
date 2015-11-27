package com.blogspot.sontx.whitelight.net;

import android.os.Handler;

import com.blogspot.sontx.libex.net.MixSocket;
import com.blogspot.sontx.libex.util.Convert;
import com.blogspot.sontx.whitelight.bean.DefConfig;
import com.blogspot.sontx.whitelight.bean.Light;
import com.blogspot.sontx.whitelight.bean.UserConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 21/11/2015.
 */
public final class ServerConnection {
    private static ServerConnection instance = null;
    private OnRefreshDataListener mOnRefreshDataListener;
    private RefreshObject refreshObject = new RefreshObject();
    private int refreshRate;
    private Handler handler = new Handler();
    private MixSocket socket = null;

    public static ServerConnection getInstance() {
        if (instance == null)
            instance = new ServerConnection();
        return instance;
    }

    public void setOnRefreshDataListener(OnRefreshDataListener listener) {
        mOnRefreshDataListener = listener;
    }

    public synchronized void setRefreshRate(int rate) {
//        refreshRate = rate;
//        if (socket != null && !socket.isClosed())
//            handler.postDelayed(refreshObject, rate);
    }

    public synchronized void connect(String ip, int port) throws IOException {
        disconnect();
        socket = new MixSocket(ip, port);
        setRefreshRate(refreshRate);
    }

    public synchronized void disconnect() {
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

    public synchronized List<Light> getAllLights() {
        ResponseUserConfigPackage response = new ResponseUserConfigPackage();
        List<Light> lights = null;
        if (sendForResult(new RequestGetUserConfigRequestPackage(), response)) {
            List<UserConfig> userConfigs = response.getUserConfigs();
            byte[] states = getLightStates();
            if (userConfigs == null || states == null || userConfigs.size() != states.length)
                return null;
            lights = new ArrayList<>(states.length);
            for (int i = 0; i < states.length; i++) {
                UserConfig userConfig = userConfigs.get(i);
                Light light = new Light();
                light.setState(states[i]);
                light.setName(userConfig.getName());
                light.setSensor(userConfig.getSensor());
                light.setExtra(userConfig.getExtra());
                light.setLstConfig(userConfig.getLstConfig());
                light.setLstTime(userConfig.getLstTime());
                light.setNConfig(userConfig.getNConfig());
                lights.add(light);
            }
            return lights;
        }
        return lights;
    }

    public synchronized List<DefConfig> getAllDefConfigs() {
        ResponseDefConfigPackage response = new ResponseDefConfigPackage();
        if (sendForResult(new RequestGetDefConfigRequestPackage(), response)) {
            return response.getDefConfigs();
        }
        return null;
    }

    public synchronized boolean updateUserConfig(UserConfig config, int id) {
        return true;
    }

    public synchronized boolean updateDefConfig(DefConfig config, int id) {
        return true;
    }

    public synchronized boolean addLight(UserConfig light) {
        return true;
    }

    public synchronized boolean removeLight(int id) {
        return true;
    }

    private synchronized boolean checkState() {
        return socket != null && !socket.isClosed();
    }

    private byte[] getLightStates() {
        ResponseLightStatePackage response = new ResponseLightStatePackage();
        if (sendForResult(new RequestGetLightStateRequestPackage(), response))
            return response.getStates();
        return null;
    }

    private synchronized boolean sendForResult(RequestPackage request, ResponsePackage response) {
        if (!checkState())
            return false;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String line = Convert.base64Encode(request.getBytes());
        int mid = line.length() / 2;
        String line1 = line.substring(0, mid);
        String line2 = line.substring(mid);
        if (!socket.send(line1 + "\r"))
            return false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!socket.send(line2 + "\r"))
            return false;
        byte[] buff = new byte[500];
        int ret = socket.receive(buff, 5000);
//        String receive = socket.receiveLine(5000);
        if (ret <= 0)
            return false;
        String receive = new String(buff, 0, ret, Charset.forName("ASCII"));
        if (receive != null) {
            buff = Convert.base64Decode(receive);
            response.init(buff);
            return true;
        }
        return false;
    }

    public interface OnRefreshDataListener {
        // refresh light state only(off/on)
        void refreshLightStates(byte[] lightStates);
    }

    private class RefreshObject implements Runnable {
        @Override
        public void run() {
            if (mOnRefreshDataListener != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] states = getLightStates();
                        if (states != null)
                            mOnRefreshDataListener.refreshLightStates(states);
                    }
                }).start();
            }
        }
    }
}
