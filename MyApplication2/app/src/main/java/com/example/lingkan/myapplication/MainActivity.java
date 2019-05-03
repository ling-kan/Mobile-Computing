package com.example.lingkan.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

//Permissions


public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //http://www.mysamplecode.com/2012/06/android-internal-external-storage.html

    //LocationActivity to  find users current location
    public void locationClick(View view) {
        //Asking for location permsision then go to request permission result
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    //camera button to open camera activity
    public void captureClick(View view) {
        Intent openCameraIntent = new Intent(this, cameraActivity.class);
        startActivity(openCameraIntent);
    }
    //city button to open city activity
    public void cityClick(View view){
        Intent openCityIntent = new Intent(this, cityActivity.class);
        startActivity(openCityIntent);
    }
    //web button to open web activity
    public void webClick(View view){
        Intent openWebIntent = new Intent(this, webAcitivity.class);
        startActivity(openWebIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                //If result is empty
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Once permission is granted , a message will appear
                    Toasty.success(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    //And will take you to the next page
                    Intent openLocationIntent= new Intent(this, locationActivity.class);
                   startActivity(openLocationIntent);
                } else {
                    //If permission is denied a alert will appear
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Permission Denied");
                    alert.setMessage("You will need to allow locations to be able to use this activity. Please try again!");
                    alert.setPositiveButton("OK", null);
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }
                return;
            }
        }
    }

}





