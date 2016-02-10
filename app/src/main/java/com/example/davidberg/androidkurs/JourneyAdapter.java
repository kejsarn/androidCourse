package com.example.davidberg.androidkurs;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

/**
 * Created by davidberg on 07/02/16.
 */
public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder> {
    protected Context mContext;
    protected SortedList<VasttrafikJourney> mJourneys;

    public JourneyAdapter() {
        mJourneys = new SortedList<VasttrafikJourney>(VasttrafikJourney.class, new SortedListAdapterCallback<VasttrafikJourney>(this) {
            @Override
            public int compare(VasttrafikJourney j0, VasttrafikJourney j1) {
                return (int)(j0.minutesUntilDeparture() - j1.minutesUntilDeparture());
            }

            @Override
            public boolean areItemsTheSame(VasttrafikJourney j0, VasttrafikJourney j1){
                return j0.getJourneyId().equals(j1.getJourneyId());
            }

            @Override
            public boolean areContentsTheSame(VasttrafikJourney oldItem, VasttrafikJourney newItem){
                return oldItem.equals(newItem);
            }
        });
    }

    public void addItem(VasttrafikJourney j){
        mJourneys.add(j);
        Log.d("VASTTRAFIK", "Journey with id: " + j.getJourneyId() + " updated.");
    }

    public void removePastDepartures(){
        VasttrafikJourney journeyInList;
        Vector<Integer> indexToRemove = new Vector();

        for(int i=0;i<mJourneys.size();i++){
            if(mJourneys.get(i).minutesUntilDeparture()<0){
                Log.d("VASTTRAFIK", "Will remove journey with id: " + mJourneys.get(i).getJourneyId());
                indexToRemove.add(i);
            }
        }

        Iterator<Integer> it = indexToRemove.iterator();
        while(it.hasNext()){
            Log.d("VASTTRAFIK", "Journey removed");
            mJourneys.removeItemAt(it.next());
        }
    }

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
    public long getItemId(int position){
        return Long.parseLong(mJourneys.get(position).getJourneyId());
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
