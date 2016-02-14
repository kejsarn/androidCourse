package com.example.davidberg.androidkurs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Comparator;
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
                long comp = (j0.minutesUntilDeparture() - j1.minutesUntilDeparture());
                Log.d("VTJOURNEYICOMP","Items compared, "+comp+": "+j0.getJourneyId()+" "+j1.getJourneyId());
                return (int)comp;
            }

            @Override
            public boolean areItemsTheSame(VasttrafikJourney j0, VasttrafikJourney j1){
                boolean retVal = j0.getJourneyId().equals(j1.getJourneyId());
                if(!retVal){
                    Log.d("VTJOURNEYIDITEMSSAME","Items not the same: "+j0.getJourneyId()+" "+j1.getJourneyId());
                }else{
                    Log.d("VTJOURNEYIDITEMSSAME","Items are the same: "+j0.getJourneyId()+" "+j1.getJourneyId());
                }
                return retVal;
            }

            @Override
            public boolean areContentsTheSame(VasttrafikJourney oldItem, VasttrafikJourney newItem){
                boolean retVal = oldItem.equals(newItem);
                if (!retVal) {
                    Log.d("VTJOURNEYIDCONTENTSSAME", "Item content not the same: "+oldItem.getJourneyId()+" "+newItem.getJourneyId());
                }
                return oldItem.equals(newItem);
            }
            @Override
            public void onInserted (int position, int count){
                Log.d("VTJOURNEYIDINSERTED", String.valueOf(count).toString()+"iItem(s) inserted at: "+String.valueOf(position));
                for(int i=position;i<position+count;i++) {
                    Log.d("VTJOURNEYIDINSERTED", "  - " + mJourneys.get(i).getJourneyId());
                }
            }
        });
    }

    public void addItem(@NonNull VasttrafikJourney j){
        int index = -1;
        for (int i = 0; i < mJourneys.size(); i++) {
            if (mJourneys.get(i).getJourneyId().equals(j.getJourneyId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            mJourneys.add(j); // add it if it doesnt exist
            Log.d("VASTTRAFIK", "Journey with id: " + j.getJourneyId() + " added.");
        } else {
            mJourneys.updateItemAt(index, j); // update it if it exists
            Log.d("VASTTRAFIK", "Journey with id: " + j.getJourneyId() + " updated.");
        }

    }

    public void addDivider(String text, Long minutesUntilDep){
        VasttrafikJourney divider = new VasttrafikJourney();
        divider.setTypeDivider(true);
        divider.setName(text);
        divider.setDividerTime(minutesUntilDep);
        divider.setJourneyId(divider.getDividerTime().toString());
        mJourneys.add(divider);
    }

    public void addAll(List<VasttrafikJourney> l){
        Iterator<VasttrafikJourney> it = l.iterator();
        while(it.hasNext()){
          addItem(it.next());
        }

        //notifyDataSetChanged();
        Log.d("VASTTRAFIK", "All Journeys added/updated.");
    }

    public void removePastDepartures(){
        VasttrafikJourney journeyInList;
        Vector<VasttrafikJourney> objectsToRemove = new Vector();


        for(int i=0;i<mJourneys.size();i++){
            if(mJourneys.get(i).minutesUntilDeparture()<0){
                Log.d("VASTTRAFIK", "Will remove j: " + mJourneys.get(i).getJourneyId()+" "+mJourneys.get(i).minutesUntilDeparture()+" "+mJourneys.get(i).getSname());
                //mJourneys.remove(mJourneys.get(i));
               objectsToRemove.add(mJourneys.get(i));
            }
        }

        mJourneys.beginBatchedUpdates();
        Iterator<VasttrafikJourney> it = objectsToRemove.iterator();
        while(it.hasNext()){
            Log.d("VASTTRAFIK", "Journey removed");
            mJourneys.remove(it.next());
        }
        mJourneys.endBatchedUpdates();
        notifyDataSetChanged();
    }

    @Override
    public JourneyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.journeylistlayout, parent, false);
            JourneyViewHolder vh = new JourneyViewHolder(v);
            return vh;
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dividerlistlayout, parent, false);
            DividerViewHolder vh = new DividerViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(JourneyAdapter.ViewHolder holder, int position) {
        VasttrafikJourney j = mJourneys.get(position);
        if(getItemViewType(position)==0){
            ((JourneyViewHolder)holder).time.setText(j.getTime());
            ((JourneyViewHolder)holder).sName.setText(j.getSname());
            ((JourneyViewHolder)holder).destination.setText(j.getDirection()+" "+j.getJourneyId());
        }else{
            ((DividerViewHolder)holder).text.setText(j.getDividerTime().toString()+""+j.getDividerText());
        }
    }

    @Override
    public int getItemViewType(int position){
        if(!mJourneys.get(position).isTypeDivider()){
            return 0;
        }else{
            return 1;
        }
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

        public ViewHolder(View v){
            super(v);
        }

    }
    public static class JourneyViewHolder extends ViewHolder{
        public TextView time;
        public TextView sName;
        public TextView destination;

       public JourneyViewHolder(View v){
           super(v);
           time = (TextView) v.findViewById(R.id.rttime);
           sName = (TextView) v.findViewById(R.id.sname);
           destination = (TextView) v.findViewById(R.id.destination);
       }
    }

    public static class DividerViewHolder extends ViewHolder{
        public TextView text;

        public DividerViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.dividerText);
        }
    }
}
