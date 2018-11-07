package com.example.chad.androidlabs;

import android.app.Activity;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.util.Xml;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import org.xmlpull.v1.XmlPullParser;
        import org.xmlpull.v1.XmlPullParserException;
        import java.io.BufferedInputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class WeatherForecast  extends Activity {
    protected static final String ACTIVITY_NAME = "WeatherActivity";
    private TextView currentTextView,lowTextView,highTextView, windSpeedTextView;
    private ImageView weatherImageView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getProgressDrawable().setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

        currentTextView = findViewById(R.id.currentTemperatureTextView);
        lowTextView = findViewById(R.id.minTemperatureTextView);
        highTextView = findViewById(R.id.maxTemperatureTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        weatherImageView = findViewById(R.id.weatherImageView);

        new ForecastQuery().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy()");
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String minT,maxT,currentT,iconName, windSpeed;
        private Bitmap weatherPicture;


        protected String doInBackground(String... arg) {
            InputStream stream = null;

            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                XmlPullParser xml = Xml.newPullParser();
                xml.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xml.setInput(conn.getInputStream(), null);

                while (xml.next() != XmlPullParser.END_DOCUMENT) {
                    if (xml.getEventType() == XmlPullParser.START_TAG) {
                        if (xml.getName().equalsIgnoreCase("temperature")) {
                            currentT = xml.getAttributeValue(null, "value");
                            Log.i(ACTIVITY_NAME, currentT);
                            publishProgress(20);
                            minT = xml.getAttributeValue(null, "min");
                            publishProgress(40);
                            maxT = xml.getAttributeValue(null, "max");
                            publishProgress(60);
                        } else if (xml.getName().equalsIgnoreCase("weather")) {
                            iconName = xml.getAttributeValue(null, "icon");
                        } else if (xml.getName().equalsIgnoreCase("speed")) {
                            windSpeed = xml.getAttributeValue(null, "value");
                            publishProgress(80);

                        }
                    }

                }


                if(getBaseContext().getFileStreamPath(iconName + ".png").exists()){

                    Log.i(ACTIVITY_NAME, iconName + ".png exists locally.. loading");

                    FileInputStream fis = null;
                    try {    fis = openFileInput(iconName + ".png");   }
                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                    weatherPicture = BitmapFactory.decodeStream(fis);

                } else {

                    Log.i(ACTIVITY_NAME, iconName + ".png does not exist locally.. downloading");

                    weatherPicture = HttpUtils.getImage("https://openweathermap.org/img/w/" + iconName + ".png");
                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    weatherPicture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    publishProgress(100);

                }


            } catch (IOException e) {
                Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i(ACTIVITY_NAME, "XmlPullParserException: " + e.getMessage());
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
                    }
                return null;
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            if (values[0] == 100) {

            }
        }

        @Override
        protected void onPostExecute(String result) {

            currentTextView.setText("Current: "+currentT+" \u2103");
            lowTextView.setText("Low: "+minT + " \u2103");
            highTextView.setText("High: "+maxT + " \u2103");
            windSpeedTextView.setText(("Wind Speed: " + windSpeed));
            weatherImageView.setImageBitmap(weatherPicture);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}