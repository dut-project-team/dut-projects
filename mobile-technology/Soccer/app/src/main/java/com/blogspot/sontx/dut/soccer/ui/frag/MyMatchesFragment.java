package com.blogspot.sontx.dut.soccer.ui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blogspot.sontx.dut.soccer.R;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;

import java.util.List;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 06/05/2016.
 */
public class MyMatchesFragment extends MatchesFragment implements AdapterView.OnItemClickListener {
    private static final String ARG_ACCOUNT_ID = "account-id";
    private int mAccountId = -1;
    private List<Match> mMatches;
    private ListView mListView;

    public MyMatchesFragment() {
    }

    public static MyMatchesFragment newInstance(int accountId) {
        MyMatchesFragment fragment = new MyMatchesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ACCOUNT_ID, accountId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mAccountId = getArguments().getInt(ARG_ACCOUNT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches_list, container, false);

        mListView = (ListView) view;
        mMatches = getMatches();
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(new MatchesAdapter(mMatches, getActivity()));

        return view;
    }

    private List<Match> getMatches() {
        List<Match> matches = DatabaseManager.getInstance().getMachesByAccountId(mAccountId);
        return matches;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnListFragmentInteractionListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Match match = mMatches.get(position);
        if (mOnListFragmentInteractionListener != null)
            mOnListFragmentInteractionListener.onListFragmentInteraction(match);
    }

    @Override
    public void onFragmentDataChanged(Object extra) {
        mMatches = getMatches();
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(new MatchesAdapter(mMatches, getActivity()));
        super.onFragmentDataChanged(extra);
    }
}
