package com.blogspot.sontx.dut.soccer.ui.dlg;

import android.app.Dialog;
import android.view.Window;

/**
 * Copyright NoEm 2016
 * Created by Noem on 6/5/2016.
 */
public abstract class NoTitleDialog extends BaseDialog {
    public NoTitleDialog(Dialog dialog) {
        super(dialog);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }
}
