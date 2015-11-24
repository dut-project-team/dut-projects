package com.blogspot.sontx.whitelight.sample;

import com.blogspot.sontx.whitelight.bean.DefConfig;
import com.blogspot.sontx.whitelight.bean.Light;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 23/11/2015.
 */
public class LightSample {
    public static List<Light> getLights() {
        List<Light> lights = new ArrayList<>();

        Light light = new Light();
        light.setNConfig((byte) 2);
        light.setState((byte) 1);
        light.setLstTime(new short[]{1000, 300, 0, 0, 0});
        light.setLstConfig(new byte[]{1, 0, 0, 0, 0});
        light.setName("light 1");
        light.setExtra((byte) 161);// 1 - 10(type-pin)
        light.setSensor((byte) 104);// 6 - 8(people-light)
        lights.add(light);

        light = new Light();
        light.setNConfig((byte) 3);
        light.setState((byte) 0);
        light.setLstTime(new short[]{1100, 200, 999, 0, 0});
        light.setLstConfig(new byte[]{1, 0, 1, 0, 0});
        light.setName("light 2");
        light.setExtra((byte) 178);// 2 - 11(type-pin)
        light.setSensor((byte) 121);// 7 - 9(people-light)
        lights.add(light);

        light = new Light();
        light.setNConfig((byte) -1);
        light.setState((byte) 0);
        light.setName("light 3");
        light.setExtra((byte) 213);// 5 - 13(type-pin)
        light.setSensor((byte) 120);// 7 - 8(people-light)
        lights.add(light);

        light = new Light();
        light.setNConfig((byte) -2);
        light.setState((byte) 1);
        light.setName("light 4");
        light.setExtra((byte) 193);// 1 - 12(type-pin)
        light.setSensor((byte) 105);// 6 - 9(people-light)
        lights.add(light);

        return lights;
    }
    public static List<DefConfig> getAllDefConfigs() {
        List<DefConfig> defConfigs = new ArrayList<>();

        DefConfig config = new DefConfig();
        config.setLightThreshold((byte) 10);
        config.setState(-878378496);
        defConfigs.add(config);

        config = new DefConfig();
        config.setLightThreshold((byte) 10);
        config.setState(-883359744);
        defConfigs.add(config);

        config = new DefConfig();
        config.setLightThreshold((byte) 10);
        config.setState(-905969664);
        defConfigs.add(config);

        config = new DefConfig();
        config.setLightThreshold((byte) 10);
        config.setState(-1040187392);
        defConfigs.add(config);

        config = new DefConfig();
        config.setLightThreshold((byte) 10);
        config.setState(-905969664);
        defConfigs.add(config);

        config = new DefConfig();
        config.setLightThreshold((byte) 10);
        config.setState(-905969664);
        defConfigs.add(config);

        config = new DefConfig();
        config.setLightThreshold((byte) 10);
        config.setState(-905969664);
        defConfigs.add(config);

        config = new DefConfig();
        config.setLightThreshold((byte) 10);
        config.setState(-905969664);
        defConfigs.add(config);

        return defConfigs;
    }
}
