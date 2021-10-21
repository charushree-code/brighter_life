package com.example.brighterlife.breathe;
import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Calendar;
public class Prefs {
    private SharedPreferences preferences;

    public  Prefs(Activity activity) {
        this.preferences = activity.getPreferences((Activity.MODE_PRIVATE));
    }
    //saving our data into system file
    public void setBreaths(int breaths) {
        preferences.edit().putInt("breaths", breaths).apply();
    }

    public int getBreaths() {
        return preferences.getInt("breaths", 0);
    }

    public void setDate(long milliseconds) {
        preferences.edit().putLong("seconds", milliseconds).apply();
    }

    public String getDate() {
        long milliDate = preferences.getLong("seconds", 0);
        String amOrPm;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliDate);
        int ap = calendar.get(Calendar.AM_PM);
        if (ap == Calendar.AM)
            amOrPm = "am";
        else
            amOrPm = "pm";

        //String time = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " + amOrPm;
        String time = String.format("%d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)) + " " + amOrPm;

        return time;
    }

    public void setSessions(int session) {
        preferences.edit().putInt("sessions", session).apply();
    }

    public int getSessions() {
        return preferences.getInt("sessions", 0);
    }
}
