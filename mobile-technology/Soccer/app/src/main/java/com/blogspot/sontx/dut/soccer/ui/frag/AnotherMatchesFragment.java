package com.blogspot.sontx.dut.soccer.ui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.blogspot.sontx.dut.soccer.R;
import com.blogspot.sontx.dut.soccer.bean.City;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;

import java.util.List;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 06/05/2016.
 */
public class AnotherMatchesFragment extends MatchesFragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private ListView mListView;
    private Spinner mSpinner;
    private List<City> mCities;
    private List<Match> mMatches;

    public AnotherMatchesFragment() {}

    public static AnotherMatchesFragment newInstance() {
        AnotherMatchesFragment fragment = new AnotherMatchesFragment();
        return fragment;
    }

    private List<Match> getMatches(int cityId) {
        List<Match> matches = DatabaseManager.getInstance().getMatchesByCityId(cityId);
        return matches;
    }

    private void loadCitiesList() {
        mCities = DatabaseManager.getInstance().getCities();
        ArrayAdapter<City> adapter = new ArrayAdapter<>(mSpinner.getContext(), android.R.layout.simple_spinner_dropdown_item, mCities);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches_another, container, false);

        mListView = (ListView) view.findViewById(R.id.lv_matches_list);
        mListView.setOnItemClickListener(this);
        mSpinner = (Spinner) view.findViewById(R.id.sp_matches_location);

        loadCitiesList();

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        City city = mCities.get(position);
        mMatches = getMatches(city.getCityId());
        MatchesAdapter adapter = new MatchesAdapter(mMatches, view.getContext());
        mListView.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Match match = mMatches.get(position);
        if (mOnListFragmentInteractionListener != null)
            mOnListFragmentInteractionListener.onListFragmentInteraction(match);
    }
}
