package com.example.davidberg.androidkurs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by davidberg on 07/02/16.
 */
public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder> {
    protected Context mContext;
    protected List<VasttrafikJourney> mJourneys;

    public JourneyAdapter(List<VasttrafikJourney> journeys) {
        mJourneys = journeys;
    }

    /*@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.journeylistlayout, null);
            holder = new ViewHolder();
            holder.destination = (TextView) convertView.findViewById(R.id.destination);
            holder.sName = (TextView) convertView.findViewById(R.id.sname);
            holder.time = (TextView) convertView.findViewById(R.id.rttime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VasttrafikJourney j = mJourneys.get(position);

        holder.time.setText(j.getTime());
        holder.sName.setText(j.getSname());
        holder.destination.setText(j.getDirection());

        return convertView;
    }*/

    @Override
    public JourneyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.journeylistlayout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(JourneyAdapter.ViewHolder holder, int position) {
        VasttrafikJourney j = mJourneys.get(position);

        holder.time.setText(j.getTime());
        holder.sName.setText(j.getSname());
        holder.destination.setText(j.getDirection());
    }

    @Override
    public int getItemCount() {
        return mJourneys.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView time;
        public TextView sName;
        public TextView destination;

        public ViewHolder(View v){
            super(v);
            time = (TextView) v.findViewById(R.id.rttime);
            sName = (TextView) v.findViewById(R.id.sname);
            destination = (TextView) v.findViewById(R.id.destination);
        }

    }
}
