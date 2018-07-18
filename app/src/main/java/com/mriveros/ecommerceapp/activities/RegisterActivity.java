package com.mriveros.ecommerceapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.mriveros.ecommerce.R;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.app.DatePickerDialog;
import android.widget.Toast;
import java.util.Calendar;



public class RegisterActivity extends AppCompatActivity {

    public static final String DATE_DIALOG_ID = "datePicker";

    // declare static int variables to store date and time
    public static Button etBirthdate;
    private static int mYear;
    private static int mMonth;
    private static int mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etLastname = (EditText) findViewById(R.id.etLastname);
        etBirthdate = (Button) findViewById(R.id.etBirthdate);
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
                                AlertDialog.Builder builder_succes = new AlertDialog.Builder(RegisterActivity.this);
                                builder_succes.setMessage("Usuario Registrado Exitosamente!")
                                        .setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    RegisterActivity.this.startActivity(intent);
                                }
                            })
                                        .create()
                                        .show();

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
                if(name.equalsIgnoreCase("") || lastname.equalsIgnoreCase("") || birtdate.equalsIgnoreCase("") || username.equalsIgnoreCase("") || password.equalsIgnoreCase("") || address.equalsIgnoreCase("") ||
                        birtdate.equalsIgnoreCase(getString(R.string.checkout_set_date)) ||neigborhood.equalsIgnoreCase("") || phone.equalsIgnoreCase("") || mail.equalsIgnoreCase("")){
                    Toast.makeText(RegisterActivity.this, R.string.form_alert, Toast.LENGTH_SHORT).show();
                }
                else{

                    queue.add(registerRequest);
                }

            }


        });

        // event listener to handle date button when pressed
        etBirthdate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show date picker dialog
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });


    }

    // method to create date picker dialog
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // get selected date
            mYear = year;
            mMonth = month;
            mDay = day;

            // show selected date to date button
            etBirthdate.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig)
    {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }



}

