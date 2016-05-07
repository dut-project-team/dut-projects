package com.blogspot.sontx.dut.soccer.ui.dlg;

import android.app.Dialog;
import android.view.ViewGroup;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 06/05/2016.
 */
public class BaseDialog {
    protected final Dialog mDialog;
    private OnDialogDataChangedListener mOnDialogDataChangedListener = null;

    public void setOnDialogDataChangedListener(OnDialogDataChangedListener listener) {
        mOnDialogDataChangedListener = listener;
    }

    protected void fireOnDialogDataChanged() {
        if (mOnDialogDataChangedListener != null)
            mOnDialogDataChangedListener.onDialogDataChanged(this);
    }

    public BaseDialog(Dialog dialog) {
        this.mDialog = dialog;
    }

    public void show() {
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    public interface OnDialogDataChangedListener {
        void onDialogDataChanged(BaseDialog dialog);
    }
}
