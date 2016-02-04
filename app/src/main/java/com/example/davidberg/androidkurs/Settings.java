package com.example.davidberg.androidkurs;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.view.View;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by davidberg on 28/01/16.
 */
public class Settings {

    static public void SaveUsernameAndIp(AppCompatActivity a, String userName, String ip){
        Log.d("CREATION", "Saving userName: "+userName+" and ip: "+ip);
        SharedPreferences pref = a.getSharedPreferences("PREF_HUE", 0);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString("UserName", userName);
        ed.putString("IP", ip);
        ed.commit();
    }

    static public String[] LoadUsernameAndIp(AppCompatActivity a){
        String[] ret = new String[2];
        SharedPreferences pref = a.getSharedPreferences("PREF_HUE", 0);
        ret[0] = pref.getString("UserName", "NULL");
        ret[1] = pref.getString("IP", "NULL");
        Log.d("CREATION", "Loaded userName: "+ret[0]+" and ip: "+ret[1]);
        return ret;
    }
}
