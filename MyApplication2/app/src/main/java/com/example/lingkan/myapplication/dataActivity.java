package com.example.lingkan.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

//https://www.youtube.com/watch?annotation_id=annotation_3065812653&feature=iv&src_vid=SK98ayjhk1E&v=aQAIMY-HzL8
public class dataActivity extends AppCompatActivity {

    private static final String TAG = "dataActivity";
    private ListView listView;
    private EditText inputSearch;

    SQLite mySQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        //Add data to datalist then use functions with 'SQLiteHelp' java file
        listView = (ListView) findViewById(R.id.listView);
        mySQL = new SQLite(this);

        Cursor data = mySQL.getAllData();
        ArrayList<String> listArray = new ArrayList<>();
        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listArray.add(data.getString(1));

        }
        //create the list adapter and set the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray);
        listView.setAdapter(adapter);
    }


}