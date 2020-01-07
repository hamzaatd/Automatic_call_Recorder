package com.example.check_phone_state;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbhelper extends SQLiteOpenHelper {

      public static final String  dbname = "call.db";
      public static final String  table_name = "call_detail";

    public dbhelper(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create  table "+table_name+"(Id Integer Primary Key Autoincrement,Number Text,Date Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("Drop Table if Exists "+table_name);

    }

    public  boolean insertdata(String nubmer,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Number",nubmer);
        values.put("Date",date);
      long result =   db.insert(table_name,null,values);

      if(result == -1)
      {

          return false;

      }
      else {


          return true;
      }

    }



     public Cursor getall(){
        SQLiteDatabase db =this.getWritableDatabase();
         Cursor cursor = db.rawQuery("select * from " + table_name, null);
         return cursor;

     }

         public Integer delete_data(){
         SQLiteDatabase db =this.getWritableDatabase();
         return   db.delete(table_name,null,null);

     }


       public Integer delete_row(String Id){
        SQLiteDatabase db =this.getWritableDatabase();
        return db.delete(table_name,"Id = ?",new String[] {Id});

    }







}
