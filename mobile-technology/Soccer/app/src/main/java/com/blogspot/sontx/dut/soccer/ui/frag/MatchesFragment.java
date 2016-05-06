package com.blogspot.sontx.dut.soccer.ui.frag;

import com.blogspot.sontx.dut.soccer.bean.Match;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 06/05/2016.
 */
public abstract class MatchesFragment extends BaseFragment {
    protected OnListFragmentInteractionListener mOnListFragmentInteractionListener;

    public void setOnListFragmentInteractionListener(OnListFragmentInteractionListener listener) {
        mOnListFragmentInteractionListener = listener;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Match item);
    }
}
