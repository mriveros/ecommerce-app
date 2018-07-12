package com.mriveros.ecommerceapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.mriveros.ecommerce.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etLastname = (EditText) findViewById(R.id.etLastname);
        final EditText etBirthdate = (EditText) findViewById(R.id.etBirthdate);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etAddress = (EditText) findViewById(R.id.etAddress);
        final EditText etCity = (EditText) findViewById(R.id.etCity);
        final EditText etBarrio = (EditText) findViewById(R.id.etBarrio);
        final EditText etPhone = (EditText) findViewById(R.id.etPhone);
        final EditText etMail = (EditText) findViewById(R.id.etMail);

        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                final String lastname = etLastname.getText().toString();
                final String birtdate = etBirthdate.getText().toString();
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String address = etAddress.getText().toString();
                final String city = etCity.getText().toString();
                final String neigborhood = etBarrio.getText().toString();
                final String phone = etPhone.getText().toString();
                final String mail = etMail.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Registro Fallido")
                                        .setNegativeButton("Volver", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(name, lastname, birtdate, username, password, address, city, neigborhood, phone, mail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}