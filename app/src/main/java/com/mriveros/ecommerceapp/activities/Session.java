package com.mriveros.ecommerceapp.activities;

import android.content.SharedPreferences;
import android.content.Context;


public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {

         prefs = cntx.getApplicationContext().getSharedPreferences("username", cntx.MODE_PRIVATE);
        //prefs = PreferenceManager.getDefaultSharedPreferences(cntx);


//        editor.apply();

    }



    public void setusername(String username) {
        prefs.edit().putString("username", username).commit();
    }

    public void setIdUsername(String id) {
        prefs.edit().putString("id", id).commit();
    }

    public String getusername() {
        String username = prefs.getString("username","");
        return username;
    }

    public String getIdUsername() {
        String id = prefs.getString("id","");
        return id;
    }
}