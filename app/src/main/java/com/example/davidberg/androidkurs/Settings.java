package com.example.davidberg.androidkurs;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.view.View;


/**
 * Created by davidberg on 28/01/16.
 */
public class Settings {

    static public void Spara(AppCompatActivity a){
        TextView tv = (TextView)a.findViewById(R.id.editText2);
        SharedPreferences pref = a.getSharedPreferences("PREF_NAME", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SparadText", tv.getText().toString());
        editor.commit();
    }

    static public void Ladda(AppCompatActivity a){
        String labelText = "NOLL";
        TextView tv = (TextView)a.findViewById(R.id.editText2);
        SharedPreferences pref = a.getSharedPreferences("PREF_NAME", 0);

        tv.setText(pref.getString("SparadText", labelText));
    }

}
