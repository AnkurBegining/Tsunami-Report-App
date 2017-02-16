package com.example.android.soonami;

import android.app.usage.UsageEvents;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    /*
    Url to get information
     */
    protected static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2012-01-01&endtime=2012-12-01&minmagnitude=6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    /*
      update the screen to display information from the given link
     */

    private void update(Words earthQuake){
        TextView eventTextView = (TextView)findViewById(R.id.eventFill_text_view);
        eventTextView.setText(earthQuake.getEvent());

        TextView dateTextView = (TextView)findViewById(R.id.dateFill_text_view);
        dateTextView.setText(earthQuake.getDate());

        TextView tsunamiAlertTextView = (TextView)findViewById(R.id.tusnamiAlertFill_text_view);
        tsunamiAlertTextView.setText(earthQuake.getTsunamiAlert());

    }
}
