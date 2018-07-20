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

    public void setIdUsername(int id) {
        prefs.edit().putInt("id", id).commit();
    }

    public void setPhone (String phone){ prefs.edit().putString("phone", phone).commit(); }

    public void setEmail (String email){ prefs.edit().putString("email", email).commit(); }

    public void setAddress (String address){ prefs.edit().putString("address", address).commit(); }

    public String getusername() {
        String username = prefs.getString("username","");
        return username;
    }

    public int getIdUsername() {
        int id = prefs.getInt("id",0);
        return id;
    }

    public String getPhone(){
        String phone = prefs.getString("phone", "");
        return phone;
    }

    public String getEmail(){
        String email = prefs.getString("email", "");
        return email;
    }

    public String getAddress(){
        String address = prefs.getString("address","");
        return address;
    }

}