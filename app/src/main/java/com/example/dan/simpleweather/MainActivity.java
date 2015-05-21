package com.example.dan.simpleweather;

import android.app.DownloadManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.tempString) TextView mTempString;
    @InjectView(R.id.summaryText) TextView mSummary;
    @InjectView(R.id.windSpeedValue) TextView mWindSpeed;
    @InjectView(R.id.windDirValue) TextView mWindDir;
    @InjectView(R.id.humidityValue) TextView mHumidity;
    @InjectView(R.id.dewPtValue) TextView mDewPt;
    @InjectView(R.id.coordTextView) TextView mCoords;
    @InjectView(R.id.dateTimeTextView) TextView mDateTime;



    private CurrentWeather mCurrentWeather;
    private Location mLocation;

    private LocationManager locationManager ;
    String provider;

    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        final Button mRefreshButton = (Button) findViewById(R.id.refreshButton);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWeather();
            }
        });

        updateWeather();

    }

    private void updateWeather() {

        mLocation = updateLocation();

        if (mLocation != null) {

            lat = mLocation.getLatitude();
            lon = mLocation.getLongitude();


            String apiKey = "0054ddc6f867a627fb6464b0c69c30dc";

            String forecastURL = "https://api.forecast.io/forecast/" + apiKey + "/" + lat + "," + lon;

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Weather data request failed (inOnfailure)", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String rawJSON = response.body().string();
                    try {
                        JSONObject parsedJSON = new JSONObject(rawJSON);
                        mCurrentWeather = parseWeatherJSON(parsedJSON);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setValues(lat, lon);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Weather data request failed (in onResponse)", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        else {
            Toast.makeText(getApplicationContext(), "Location not available", Toast.LENGTH_LONG).show();

        }

    }

    private Location updateLocation() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, true);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);

        Location location = locationManager.getLastKnownLocation(provider);

        // Remove the listener you previously added
        locationManager.removeUpdates(locationListener);

        return location;

    }

    private void setValues(double lat, double lon) {
        mTempString.setText(mCurrentWeather.getTemp() + " F");
        mSummary.setText(mCurrentWeather.getSummary());
        mWindSpeed.setText(mCurrentWeather.getWindSpeed() + " mph");
        mWindDir.setText(mCurrentWeather.getWindDir() + " deg");
        mHumidity.setText(mCurrentWeather.getHumidity() + " %");
        mDewPt.setText(mCurrentWeather.getDewPoint() + " F");
        double latRounded = (double) Math.round(lat * 100000)/100000;
        double lonRounded = (double) Math.round(lon * 100000)/100000;
        mCoords.setText(latRounded + ", " + lonRounded);

        mCurrentWeather.setConditionDateTime();
        mDateTime.setText(mCurrentWeather.getConditionDateTime());

    }

    private CurrentWeather parseWeatherJSON(JSONObject weatherJSON) throws JSONException {

        CurrentWeather weather = new CurrentWeather();
        JSONObject current = weatherJSON.getJSONObject("currently");

        weather.setTemp((int) current.getDouble("temperature"));
        weather.setSummary(current.getString("summary"));
        weather.setWindSpeed((int) current.getDouble("windSpeed"));
        weather.setWindDir((int) current.getDouble("windBearing"));
        weather.setDewPoint((int) current.getDouble("dewPoint"));
        weather.setHumidity((int)
                (current.getDouble("humidity") * 100));
        weather.setConditionUnixTIme(current.getLong("time"));
        weather.setTimeZone(weatherJSON.getString("timezone"));


        return weather;

    }


}
