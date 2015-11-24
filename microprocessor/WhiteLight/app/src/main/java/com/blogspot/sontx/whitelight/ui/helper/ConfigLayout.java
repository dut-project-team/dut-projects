package com.blogspot.sontx.whitelight.ui.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;

/**
 * Copyright by NE 2015.
 * Created by noem on 23/11/2015.
 */
public abstract class ConfigLayout {
    protected Context context;
    private int subTree;

    protected ConfigLayout(int subTree) {
        this.subTree = subTree;
    }

    protected abstract void onApplyLayout(View view);

    public void applyLayout(ViewStub viewStub) {
        context = viewStub.getContext();
        viewStub.setLayoutResource(subTree);
        onApplyLayout(viewStub.inflate());
    }
}
