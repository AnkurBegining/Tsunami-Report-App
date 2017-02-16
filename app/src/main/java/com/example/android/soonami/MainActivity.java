package com.example.android.soonami;

import android.app.usage.UsageEvents;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
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
        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();


    }

    /*
      update the screen to display information from the given link
     */

    private void updateUi (Words earthQuake) {
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
        /*
        Make http reponse and get string response back
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse="";
            if(url== null){
                return jsonResponse;
            }
            HttpURLConnection urlConnection =null;
            InputStream inputStream =null;
            try {
                urlConnection= (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if(urlConnection.getResponseCode()==200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
                else{
                    Log.e(LOG_TAG,"Error with response code "+urlConnection.getResponseCode());
                }


            }catch (Exception e){
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }


            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private Words extractFeatureFromJson(String jsonResponse) {
            if(TextUtils.isEmpty(jsonResponse)){
                return null;
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);
                JSONArray featureArray = baseJsonResponse.getJSONArray("features");

                // If there are results in the features array
                if (featureArray.length() > 0) {
                    // Extract out the first feature (which is an earthquake)
                    JSONObject firstFeature = featureArray.getJSONObject(0);
                    JSONObject properties = firstFeature.getJSONObject("properties");

                    // Extract out the title, time, and tsunami values
                    String title = properties.getString("title");
                    long time = properties.getLong("time");
                    int tsunamiAlert = properties.getInt("tsunami");

                    // Create a new {@link Event} object
                    return new Words(title, time, tsunamiAlert);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Words earthquake) {
            if (earthquake == null) {
                return;
            }

            updateUi(earthquake);
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
