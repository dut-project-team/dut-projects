package com.blogspot.sontx.dut.soccer.ui.frag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.sontx.dut.soccer.R;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.utils.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/04/2016.
 */
public class MatchesAdapter extends BaseAdapter {
    private List<Match> mMatches;
    private LayoutInflater mInflater;

    public MatchesAdapter(List<Match> matches, Context context) {
        mMatches = matches;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public int getCount() {
        return mMatches.size();
    }

    @Override
    public Object getItem(int position) {
        return mMatches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_matches_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Match item = mMatches.get(position);
        if (item.isVerified()) {
            int stateImageId = item.getNumberOfAvailableSlots() > 0 ? R.drawable.slot_is_available : R.drawable.slot_is_not_available;
            holder.mStateView.setImageResource(stateImageId);
        } else {
            holder.mStateView.setImageResource(R.drawable.match_not_verified);
        }
        holder.mStartTimeView.setText(DateTime.getFriendlyString(item.getStartTime()));
        holder.mAvailableSlotsView.setText(String.format("%d/%d slots are available", item.getNumberOfAvailableSlots(), item.getNumberOfSlots()));
        holder.mMoneyView.setText(String.format("%d VND", item.getMoneyPerSlot()));

        return convertView;
    }

    static class ViewHolder {
        public final View mView;
        public final ImageView mStateView;
        public final TextView mStartTimeView;
        public final TextView mAvailableSlotsView;
        public final TextView mMoneyView;

        public ViewHolder(View view) {
            mView = view;
            mStateView = (ImageView) view.findViewById(R.id.iv_matches_item_state);
            mStartTimeView = (TextView) view.findViewById(R.id.tv_matches_item_start_time);
            mAvailableSlotsView = (TextView) view.findViewById(R.id.tv_matches_item_available_slots);
            mMoneyView = (TextView) view.findViewById(R.id.tv_matches_item_money);
        }
    }
}
