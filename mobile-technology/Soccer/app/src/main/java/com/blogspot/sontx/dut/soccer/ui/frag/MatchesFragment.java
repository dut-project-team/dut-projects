package com.blogspot.sontx.dut.soccer.ui.frag;

import android.app.Fragment;
import android.content.Context;
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

public class MatchesFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_CITY_ID = "city-id";
    private int mCityId = 1;
    private List<Match> mMatches;
    private OnListFragmentInteractionListener mListener;

    public void setOnListFragmentInteractionListener(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    public MatchesFragment() {
    }

    public static MatchesFragment newInstance(int cityId) {
        MatchesFragment fragment = new MatchesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CITY_ID, cityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCityId = getArguments().getInt(ARG_CITY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches_list, container, false);

        ListView listView = (ListView) view;
        mMatches = getMatches();
        listView.setOnItemClickListener(this);
        listView.setAdapter(new MatchesAdapter(mMatches, getActivity()));

        return view;
    }

    private List<Match> getMatches() {
        List<Match> matches = DatabaseManager.getInstance().getMatchesByCityId(mCityId);
        return matches;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Match match = mMatches.get(position);
        if (mListener != null)
            mListener.onListFragmentInteraction(match);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Match item);
    }
}
