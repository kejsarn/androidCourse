package com.example.davidberg.androidkurs;

/**
 * Created by davidberg on 07/02/16.
 */
public class VasttrafikJourney {
    private String journeyId;
    private String name;
    private String sname;
    private String direction;
    private String time;

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

}
