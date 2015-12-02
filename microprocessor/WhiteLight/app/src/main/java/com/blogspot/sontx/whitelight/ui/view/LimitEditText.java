package com.blogspot.sontx.whitelight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Copyright by NE 2015.
 * Created by noem on 24/11/2015.
 */
public class LimitEditText extends EditText {
    private int maxCharacters;

    public LimitEditText(Context context) {
        super(context);
    }

    public LimitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LimitEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(getText().length() >= maxCharacters)
            return true;
        return super.onKeyDown(keyCode, event);
    }
}
