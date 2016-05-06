package com.blogspot.sontx.dut.soccer.ui.frag;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

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
        mSpinner.setAdapter(new CityAdapter(mSpinner.getContext(), mCities));
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

    private static class CityAdapter implements SpinnerAdapter {
        private List<City> mCities;
        private Context mContext;

        public CityAdapter(Context context, List<City> cities) {
            mContext = context;
            this.mCities = cities;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mContext);
            textView.setText(mCities.get(position).getName());
            return textView;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return mCities.size();
        }

        @Override
        public Object getItem(int position) {
            return mCities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mCities.get(position).getCityId();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mContext);
            textView.setText(mCities.get(position).getName());
            return textView;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
