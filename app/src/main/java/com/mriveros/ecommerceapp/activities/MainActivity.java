package com.mriveros.ecommerceapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.onesignal.OneSignal;
import com.mriveros.ecommerce.R;
import com.mriveros.ecommerceapp.analytics.Analytics;
import com.mriveros.ecommerceapp.fragments.FragmentHome;
import com.mriveros.ecommerceapp.utilities.DBHelper;
import com.mriveros.ecommerceapp.utilities.utils;

import java.io.IOException;

import android.view.View;


public class MainActivity extends AppCompatActivity {

    private Session session;//global variable
    static DBHelper dbhelper;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String idUser = intent.getStringExtra("id");
        String phone = intent.getStringExtra("phone");
        String email = intent.getStringExtra("email");
        String address = intent.getStringExtra("address");

        session = new Session(getApplicationContext()); //in oncreate
        session.setIdUsername(Integer.parseInt(idUser));
        session.setusername(username);
        session.setPhone(phone);
        session.setEmail(email);
        session.setAddress(address);


        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                String text = "OneSignal UserID:\n" + userId + "\n\n";

                if (registrationId != null)
                    text += "Google Registration Id:\n" + registrationId;
                else
                    text += "Google Registration Id:\nCould not subscribe for push";

                TextView textView = (TextView) findViewById(R.id.debug_view);
                textView.setText(text);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.main_drawer);

        //View navHeaderView = mNavigationView.inflateHeaderView(R.layout.nav_drawer_header);
        View navHeaderView =  mNavigationView.getHeaderView(0);
        TextView headerUserName = (TextView) navHeaderView.findViewById(R.id.tx_name);
        headerUserName.setText(username);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frame_container, new FragmentHome()).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                //mDrawerLayout.closeDrawers();
                //setTitle(menuItem.getTitle());

                if (menuItem.getItemId() == R.id.product) {
                    startActivity(new Intent(getApplicationContext(), ActivityMenuCategory.class));
                }

                if (menuItem.getItemId() == R.id.cart) {
                    startActivity(new Intent(getApplicationContext(), ActivityCart.class));
                }

                if (menuItem.getItemId() == R.id.checkout) {
                    startActivity(new Intent(getApplicationContext(), ActivityCheckout.class));
                }

                if (menuItem.getItemId() == R.id.profile) {
                    startActivity(new Intent(getApplicationContext(), ActivityProfile.class));
                }

                if (menuItem.getItemId() == R.id.information) {
                    startActivity(new Intent(getApplicationContext(), ActivityInformation.class));
                }

                if (menuItem.getItemId() == R.id.about) {
                    startActivity(new Intent(getApplicationContext(), ActivityAbout.class));
                }
                if (menuItem.getItemId() == R.id.exit) {
                    finish();
                    System.exit(0);
                }

                return false;
            }

        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        // init analytics tracker
        ((Analytics) getApplication()).getTracker();

        // checking internet connection
        if (!utils.isNetworkAvailable(MainActivity.this)) {
            Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        dbhelper = new DBHelper(this);

        // create database
        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        // then, the database will be open to use
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        // if user has already ordered food previously then show activity_confirm dialog
        if (dbhelper.isPreviousDataExist()) {
            showAlertDialog();
        }

        isStoragePermissionGranted();

    }

    // show activity_confirm dialog to ask user to delete previous order or not
    void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(getString(R.string.db_exist_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.option_yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // delete order data when yes button clicked
                dbhelper.deleteAllData();
                dbhelper.close();

            }
        });

        builder.setNegativeButton(getString(R.string.option_no), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // close dialog when no button clicked
                dbhelper.close();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            dbhelper.deleteAllData();
            dbhelper.close();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case android.R.id.home:
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

}
