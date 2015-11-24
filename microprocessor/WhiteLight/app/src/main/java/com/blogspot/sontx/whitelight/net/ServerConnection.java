package com.blogspot.sontx.whitelight.net;

import com.blogspot.sontx.whitelight.bean.DefConfig;
import com.blogspot.sontx.whitelight.bean.Light;
import com.blogspot.sontx.whitelight.bean.UserConfig;

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
}
