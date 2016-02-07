package com.example.davidberg.androidkurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by davidberg on 07/02/16.
 */
public class JourneyAdapter extends ArrayAdapter<VasttrafikJourney> {
    protected Context mContext;
    protected List<VasttrafikJourney> mJourneys;

    public JourneyAdapter(Context context, List<VasttrafikJourney> journeys) {
        super(context, R.layout.journeylistlayout, journeys);
        mContext = context;
        mJourneys = journeys;
    }

    @Override
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
    }


    public static class ViewHolder {
        TextView time;
        TextView sName;
        TextView destination;
    }
}
