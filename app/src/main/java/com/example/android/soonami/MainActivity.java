package com.example.android.soonami;

import android.app.usage.UsageEvents;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

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

    private void update(Words earthQuake) {
        TextView eventTextView = (TextView) findViewById(R.id.eventFill_text_view);
        eventTextView.setText(earthQuake.getEvent());

        TextView dateTextView = (TextView) findViewById(R.id.dateFill_text_view);
        dateTextView.setText(formatTime(earthQuake.getDate()));

        TextView tsunamiAlertTextView = (TextView) findViewById(R.id.tusnamiAlertFill_text_view);
        tsunamiAlertTextView.setText(checkCritical(earthQuake.getTsunamiAlert()));

    }

    private String checkCritical(int tsunamiAlert) {
        switch (tsunamiAlert) {
            case 0:
                return "No";

            case 1:
                return "Yes";

            default:
                return "Not Available";
        }
    }

    private String formatTime(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss z");
        return formatter.format(date);
    }

    private class TsunamiAsyncTask extends AsyncTask<URL, Void, Words >{

        @Override
        protected Words doInBackground(URL... urls) {
            //create URL object
            URL url = createUrl(USGS_REQUEST_URL);

            //perform HTTP and get json Response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (Exception e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            Words earthquake = extractFeatureFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return earthquake;

        }

        private Words extractFeatureFromJson(String jsonResponse) {
            return null;
        }

        private String makeHttpRequest(URL url) {
            return null;
        }

        private URL createUrl(String usgsRequestUrl) {
            URL url = null;
            try {
                url = new URL(usgsRequestUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return  url;

        }


    }
}
