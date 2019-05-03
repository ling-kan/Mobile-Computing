package com.example.lingkan.myapplication;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class cityActivity extends AppCompatActivity {

    //URL to get cities JSON
    //https://www.androidhive.info/2012/01/android-json-parsing-tutorial/
    // Define ListView for city list
   private ListView cityListView;
    private EditText inputSearch;
    private  ArrayAdapter adapter;

    // array list to store items from web
    ArrayList<String> cityList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

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

            new AsyncTaskParseJson().execute();
            return true;
        } else  if (
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            //If the network is not connected a toast message will appear
        AlertDialog.Builder alert = new AlertDialog.Builder(cityActivity.this);
            alert.setTitle("Error. Network connection error!");
            alert.setMessage("Unable to connect to a network! Please turn on your network, and try again!");
            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    cityActivity.this.finish();

                }
            });
            alert.show();
            return false;
        }
        return false;
    }

    //Adding AsyncTaskParseJson method
    public class AsyncTaskParseJson extends AsyncTask<String, String, String > {
        String url = "https://pkgstore.datahub.io/core/world-cities/world-cities_json/data/5b3dd46ad10990bca47b04b4739a02ba/world-cities_json.json";

        @Override
        protected void onPreExecute() {
            Toasty.info(cityActivity.this, "Processing....Please Wait...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... arg0)  {

            try {
                // creating an instance of the httpConnection class
                httpConnection jParser = new httpConnection();

                // get json string from service url
                String json = jParser.getJSONFromUrl(url);

                // parse returned json string into json array
                JSONArray jsonArray = new JSONArray(json);

                // looping the json array and add each city to item in the list
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject j = jsonArray.getJSONObject(i);

                    if (j != null) {

                        // adding name , sub country and country information
                        cityList.add(j.getString("name")+ "\n"+
                                j.getString("subcountry") + " , "+
                                j.getString("country"));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        // below method will run when service HTTP request is complete, will then bind tweet text in arrayList to ListView
        protected void onPostExecute(String string) {
            cityListView = (ListView) findViewById(R.id.cityListView);
            inputSearch=(EditText)findViewById(R.id.inputSearch);
            adapter = new ArrayAdapter(cityActivity.this, android.R.layout.simple_expandable_list_item_1, cityList);
            cityListView.setAdapter(adapter);
            //Search filter
            inputSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s,  int start, int count, int after) {
                    //When text is entered in the search filter this will also filter the informaiton
                     adapter.getFilter().filter(s);
                }
                @Override
                public void beforeTextChanged(CharSequence s,  int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }


}
