package com.example.lingkan.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class locationActivity extends AppCompatActivity {

    //https://www.android-examples.com/change-textview-text-programmatically-in-android/
    //https://www.youtube.com/watch?v=gnEnOMCwNDo
    //https://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/

    Geocoder geocoder;
    List<Address> addresses;
    double latitude = 0;
    double longitude = 0;
    int REQUEST_PLACE_PICKER = 1;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    SQLite mySQL = new SQLite(this);
    private TextView textLocation, textAddress, textPicker;
    private Button addButton, viewButton,pickerButton;
    private Intent googleMapIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create the TestActivity activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //Insert Data
        addButton = (Button) findViewById(R.id.addButton);
        viewButton = (Button) findViewById(R.id.viewButton);
        pickerButton = (Button) findViewById(R.id.pickerButton);
        textLocation = (TextView) findViewById(R.id.textLocation);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textPicker = (TextView) findViewById(R.id.textPicker);

        getInfo();
    }

    //https://www.androidauthority.com/use-sqlite-store-data-app-599743/
    //http://www.theappguruz.com/blog/android-using-sqlite-database

    //Button to open data Activity which shows database
    public void viewClick(View view) {
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewIntent = new Intent(locationActivity.this, dataActivity.class);
                    startActivity(viewIntent);
                }
            });

    }
    //Button to insert new data
    public void addClick(View view) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            boolean insertData = mySQL.insertRecord(textAddress.getText().toString());
             if (insertData) {
                Toasty.success(locationActivity.this, "Data Added!", Toast.LENGTH_SHORT).show();
            } else {
                Toasty.error(locationActivity.this,"Error!", Toast.LENGTH_SHORT).show();
            }
        }
        });
    }
    //Button to open googlemaps to find current location, and address
    public void googleMapClick (View view) {
        getInfo();
        //Open google map intent
        googleMapIntent = new Intent(Intent.ACTION_VIEW);
        startActivity(googleMapIntent);

    }
  //Button to open a location picker to find the next location
    public void pickerClick(View view) {
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkNetworkConnection();
            }
        });
    }


    public void getInfo() {
        LocationManager locationManager = (LocationManager ) this.getSystemService(Context.LOCATION_SERVICE);
        //Use the GPS to find location
        String locationProvider;
        locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        // look for last known  location
        if (lastKnownLocation == null) {
            //if no location if found
            textLocation.setText("Location could not be found!");
            textAddress.setText("Error! No Address found!\n Click 'Get Locations'!");

        } else {
            // if last location exists then get/set the latitude and logitude
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();


            //the process of getting street address with location (longitude and latitude co-ordinates)
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                //Get adddress line, city, county postocde and country from gecoder, lat longi
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAdminArea();
                String county = addresses.get(0).getLocality();
                String postcode = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryCode();
                //Get Current Date and time
                SimpleDateFormat DandT = new SimpleDateFormat("dd/MM/yyyy HH : mm");
                String currentDateandTime = DandT.format(new Date());

                //Display in Text //  Display Address
                textLocation.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
                textAddress.setText(address + ", " + county + "\n" + city + ", " + postcode + ", " + country + "\n" +  currentDateandTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Check network connectionm if connected place picker will then be launched if not an error message
    private boolean checkNetworkConnection() {
        ConnectivityManager connect = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // If the network is connected and successful which will appear
            Toasty.success(this, "Network Connection Connected ", Toast.LENGTH_LONG).show();
            //Once connection is established Place picker is launched
            try {
                //Launch PlacePicker
                PlacePicker.IntentBuilder intentBuilder =
                        new PlacePicker.IntentBuilder();
                Intent pickerIntent = intentBuilder.build(locationActivity.this);
                // Start the intent by requesting a result, identified by a request code.
                startActivityForResult(pickerIntent, REQUEST_PLACE_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
            return true;
        } else if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            //If no network is found an error message will appear.
            AlertDialog.Builder alert = new AlertDialog.Builder(locationActivity.this);
            alert.setTitle("Unable to connect to a network!");
            alert.setMessage("Network connection is needed for this activity. Please turn on your network, and try again!");
            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Alert will not do anything once it has been clicked.
                }
            });
            alert.show();
            return false;
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER  && resultCode == Activity.RESULT_OK) {
            // Once user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            textPicker.setText("Location Name:\n" + name + "\n\nAddress:\n"+ address);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
