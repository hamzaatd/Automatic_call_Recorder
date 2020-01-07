package com.example.check_phone_state;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Switch state;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    myadapter adapter;
    RecyclerView rv;
    ArrayList<File> callfiles;
    ArrayList<String> number;

    ListView listView;
    String []songnames;
     dbhelper helper ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
        rv =findViewById(R.id.audiolist);
         toolbar =findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         state =findViewById(R.id.on);
         helper =new dbhelper(this);
         callfiles =new ArrayList<>();
         number = new ArrayList<>();
         rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));

// code for delete data from sqlite

         /* int a =helper.delete_data();

          if(a>0){
           Toast.makeText(this, "data deleted", Toast.LENGTH_SHORT).show();
          }

          else {
            Toast.makeText(this, "data not deleted", Toast.LENGTH_SHORT).show();

          }
*/




      if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
          if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) !=
                  PackageManager.PERMISSION_GRANTED) {
              ActivityCompat.requestPermissions(MainActivity.this,
                      new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
                              Manifest.permission.RECORD_AUDIO, Manifest.permission.PROCESS_OUTGOING_CALLS,
                              Manifest.permission.WRITE_EXTERNAL_STORAGE},
                      10);
          }
      }

         state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked) {
                     sharedPreferences = getSharedPreferences("State1", Context.MODE_PRIVATE);
                     editor = sharedPreferences.edit();
                     editor.putString("chk_state", "true");
                     editor.commit();
                     state.setHighlightColor(Color.BLUE);
                     Toast.makeText(MainActivity.this, "value is true", Toast.LENGTH_SHORT).show();


                 } else {
                     editor.putString("chk_state", "false");
                     editor.commit();
                     Toast.makeText(MainActivity.this, "value is false", Toast.LENGTH_SHORT).show();
                 }


             }
         });

         callfiles =  readsongs(Environment.getExternalStorageDirectory());
         number =showdata();
         adapter =new myadapter(MainActivity.this,callfiles,number);
         rv.setAdapter(adapter);



       SharedPreferences preferences = getSharedPreferences("State1", Context.MODE_PRIVATE);
         String test = preferences.getString("chk_state", "");
         Toast.makeText(this, "" + test, Toast.LENGTH_SHORT).show();
       if(test.equals("true"))
       {

        state.setChecked(true);


       }
      else {


      state.setChecked(false);

       }








 }

    private ArrayList<String> showdata(){
    ArrayList<String> list =new ArrayList<>();
    Cursor c = helper.getall();

    if(c.getCount()== 0)
    {
       Toast.makeText(this, "data not present", Toast.LENGTH_SHORT).show();

    }
   else {

        while (c.moveToNext()) {

            String number = c.getString(0)+"@"+c.getString(1) + "@" + c.getString(2);
            list.add(number);

        }
    }
        return list;

   }


    private ArrayList<File> readsongs(File root){
        ArrayList<File> list =new ArrayList<File>();
        File[] files = root.listFiles();
        for(File file :files)
        {
            if(file.isDirectory())
            {
               String name =file.getName().toString();

                if(name.equals("TestRecordingData") || name.equals("OutgoingCallData")) {
                 list.addAll(readsongs(file));
                }
            }
            else  if(file.getName().endsWith(".amr"))
            {
                list.add(file);
            }
        }

        return list;
    }








}
