package com.example.android.soonami;

/**
 * Created by Lenovo on 16-02-2017.
 */

public class Words {
    private String event;
    private long date;
    private int tsunamiAlert;

    public Words(String event, long date, int tsunamiAlert){
        this.event = event;
        this.date = date;
        this.tsunamiAlert = tsunamiAlert;
    }

    public String getEvent(){
        return event;
    }

    public long getDate(){
        return date;
    }

    public int getTsunamiAlert(){
        return tsunamiAlert;
    }
}
