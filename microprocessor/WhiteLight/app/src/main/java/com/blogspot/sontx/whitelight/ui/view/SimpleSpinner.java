package com.blogspot.sontx.whitelight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.Spinner;

/**
 * Copyright by NE 2015.
 * Created by noem on 23/11/2015.
 */
public class SimpleSpinner extends Spinner {
    public SimpleSpinner(Context context) {
        super(context);
    }

    public SimpleSpinner(Context context, int mode) {
        super(context, mode);
    }

    public SimpleSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SimpleSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public void setSelectionText(String text) {
        Adapter adapter = getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            Object obj = adapter.getItem(i);
            String item = obj.toString();
            if (item.equals(text)) {
                setSelection(i);
                break;
            }
        }
    }
}
