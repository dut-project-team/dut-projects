package com.blogspot.sontx.dut.game.lib;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 18/1/2016.
 */
public final class SoundManager {
    private static SoundPool mSoundPool;
    private static HashMap<Integer, Integer> mSoundPoolMap;
    private static AudioManager mAudioManager;
    private static Context mContext;

    public static void createInstance(Context context) {
        new SoundManager(context);
    }

    public static void destroyInstance() {
        reset();
        mSoundPool.release();
    }

    private SoundManager(Context context) {
        mContext = context;
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new HashMap<>();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public static void reset() {
        mSoundPoolMap.clear();
        for (int id : mSoundPoolMap.values()) {
            mSoundPool.unload(id);
        }
        mSoundPoolMap.clear();
    }

    public static void addSound(int index, int soundId) {
        mSoundPoolMap.put(index, mSoundPool.load(mContext, soundId, 1));
    }

    public static void playSound(int index) {
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
    }

    public static void playLoopedSound(int index) {
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
    }
}
