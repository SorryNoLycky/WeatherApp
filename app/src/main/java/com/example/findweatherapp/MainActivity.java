package com.example.findweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CDATASection;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.SplittableRandom;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetDataFromInternet.AsyncResponse {

    private static final String TAG = "MainActivity";

    private Button searchButton;
    private EditText searchField;
    private TextView cityName;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.searchField);
        cityName = findViewById(R.id.cityName);
        searchButton=findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
            //URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Orel&appid=bc3c769785e409da05b5fca6ab83f12b");
            URL url = buildUrl(searchField.getText().toString());

            cityName.setText(searchField.getText().toString());

            new GetDataFromInternet(this).execute(url);
    }

    private URL buildUrl (String city){

        String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
        String PARAM_CITY = "q";
        String PARAM_APPID = "appid";
        String appid_value = "497130c54311a9b3620697166bedd1c6";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PARAM_CITY, city).appendQueryParameter(PARAM_APPID, appid_value).build();
        URL url = null;

        try {
            url=new URL(builtUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;


    }


    @Override
    public void processFinish(String output) {

        Log.d(TAG, "processFinish: "+output);
        try {

            JSONObject resultJSON = new JSONObject(output);
            JSONObject weather = resultJSON.getJSONObject("main");
            JSONObject sys = resultJSON.getJSONObject("sys");


            TextView temp = findViewById(R.id.tempValue);
            String temp_K = weather.getString("temp");
            float temp_C = Float.parseFloat(temp_K);
            temp_C = temp_C - (float)273.15;
            String temp_C_string = Float.toString(temp_C);
            temp.setText(temp_C_string);


            TextView pressure = findViewById(R.id.pressureValue);
            pressure.setText(weather.getString("pressure"));


            TextView sunrise = findViewById(R.id.timeSunrise);
            String timeSunrise = sys.getString("sunrise");
            Locale myLocale = new Locale("ru", "RU");
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", myLocale);


            String dateString = formatter.format (new Date (Long.parseLong(timeSunrise) *1000+(60 * 60 * 1000)*3));
            sunrise.setText(dateString);


            TextView sunset = findViewById(R.id.timeSunset);
            String timeSunset = sys.getString("sunset");
            //Locale myLocale = new Locale("ru", "RU");
            //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", myLocale);


            dateString = formatter.format (new Date (Long.parseLong(timeSunset) *1000+(60 * 60 * 1000)*3));
            sunset.setText(dateString);


        }catch (JSONException e){

            e.printStackTrace();

        }

    }




}