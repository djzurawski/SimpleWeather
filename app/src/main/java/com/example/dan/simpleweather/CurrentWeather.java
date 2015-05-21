package com.example.dan.simpleweather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dan on 5/20/2015.
 */
public class CurrentWeather {

    private int mTemp;
    private int mWindSpeed;
    private int mWindDir;
    private int mHumidity;
    private int mDewPoint;
    private String mSummary;
    private long mConditionUnixTIme;
    private String mConditionDateTime;
    private String mTimeZone;

    public void setConditionDateTime() {
        Date date = new Date(mConditionUnixTIme*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a z  MM-dd-yyyy"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone(mTimeZone)); // give a timezone reference for formating (see comment at the bottom
        mConditionDateTime = sdf.format(date);

    }

    public String getConditionDateTime() {
        return mConditionDateTime;
    }

    public long getConditionUnixTIme() {
        return mConditionUnixTIme;
    }

    public void setConditionUnixTIme(long conditionUnixTIme) {
        mConditionUnixTIme = conditionUnixTIme;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getTempString() {
        return mTemp  + " F";
    }

    public String getWindSpeedString() {
        return mWindSpeed + " mph";
    }

    public String getWindDirString() {
        return mWindDir + " deg";
    }

    public String getHumidityString() {
        return mHumidity + " %";
    }


    public int getTemp() {
        return mTemp;
    }

    public void setTemp(int temp) {
        mTemp = temp;
    }

    public int getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        mWindSpeed = windSpeed;
    }

    public int getWindDir() {
        return mWindDir;
    }

    public void setWindDir(int windDir) {
        mWindDir = windDir;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }

    public int getDewPoint() {
        return mDewPoint;
    }

    public void setDewPoint(int dewPoint) {
        mDewPoint = dewPoint;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
