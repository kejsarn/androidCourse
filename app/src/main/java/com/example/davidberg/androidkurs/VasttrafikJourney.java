package com.example.davidberg.androidkurs;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by davidberg on 07/02/16.
 */
public class VasttrafikJourney {
    private String journeyId;
    private String name;
    private String sname;
    private String direction;
    private String time;
    private String date;
    private boolean typeDivider = false;
    private String dividerText;
    private Long dividerTime;

    public Long getDividerTime() {
        return dividerTime;
    }

    public void setDividerTime(Long dividerTime) {
        this.dividerTime = dividerTime;
    }

    public String getDividerText() {
        return dividerText;
    }

    public void setDividerText(String dividerText) {
        this.dividerText = dividerText;
    }

    public boolean isTypeDivider() {
        return typeDivider;
    }

    public void setTypeDivider(boolean typeDivider) {
        this.typeDivider = typeDivider;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String joerneyId) {
        this.journeyId = joerneyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long minutesUntilDeparture(){
        if(isTypeDivider()){
            return getDividerTime();
        }else {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            long timeDiffMilliSec = 0;
            try {
                Date date = (Date) formatter.parse(getDate() + " " + getTime());
                timeDiffMilliSec = date.getTime() - Calendar.getInstance().getTimeInMillis();
            } catch (ParseException e) {
                Log.e("VASTTRAFIK", "Error parsing rtTime! String to parse: " + getDate() + " " + getTime());
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                Log.e("VASTTRAFIK", "Error converting Calendar to milliseconds");
            }
            return timeDiffMilliSec / (1000 * 60);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof VasttrafikJourney)) return false;
        VasttrafikJourney otherMyClass = (VasttrafikJourney) other;
        if (otherMyClass.getJourneyId().equals(this.getJourneyId()) &&
                otherMyClass.getTime().equals(this.getTime()) &&
                otherMyClass.getDate().equals(this.getDate()) &&
                otherMyClass.getDirection().equals(this.getDirection()) &&
                otherMyClass.getName().equals(this.getName()) &&
                otherMyClass.getSname().equals(this.getSname()) &&
                otherMyClass.isTypeDivider()==this.isTypeDivider()) return true;
        return false;
    }

}
