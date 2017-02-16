package com.example.android.soonami;

/**
 * Created by Lenovo on 16-02-2017.
 */

public class Words {
    private String event;
    private String date;
    private String tsunamiAlert;

    public Words(String event, String date, String tsunamiAlert){
        this.event = event;
        this.date = date;
        this.tsunamiAlert = tsunamiAlert;
    }

    public String getEvent(){
        return event;
    }

    public String getDate(){
        return date;
    }

    public String getTsunamiAlert(){
        return tsunamiAlert;
    }
}
