package com.example.lingkan.myapplication;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class webAcitivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_acitivity);

    checkNetworkConnection();
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connect = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // If the network is connected and successful which will appear
            Toasty.success(this, "Network Connection Connected ", Toast.LENGTH_LONG).show();

            //Open TripAdvisor webpage and display in webView
            webView = (WebView) findViewById(R.id.webView);
            String url = "http://www.tripadvisor.co.uk/";
            webView.getSettings().setJavaScriptEnabled(true);
            // load a website in a webview
            webView.loadUrl(url);

            return true;
        } else  if (
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            //If the network is not connected a toast message will appear
            AlertDialog.Builder alert = new AlertDialog.Builder(webAcitivity.this);
            alert.setTitle("Error. Network connection error!");
            alert.setMessage("Unable to connect to a network! Please turn on your network, and try again!");
            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    webAcitivity.this.finish();

                }
            });
            alert.show();
            return false;
        }
        return false;
    }
}
