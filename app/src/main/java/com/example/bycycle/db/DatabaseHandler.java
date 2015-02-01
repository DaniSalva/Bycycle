package com.example.bycycle.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context, String name,CursorFactory factory, int version)
    {

        super(context, name, factory, version);
    }
    public String[] tables={"User","Route","Competition","Belong","Track","Beplaced"};
    public String[] triggers={"UpdateKm","UpdateCoins","UpdateTrips"};

    public static final String USER_CREATE_TABLE =
            "CREATE TABLE User (                   	            " +
                    "nickname       VARCHAR(50) PRIMARY KEY ,   " +
                    "password       VARCHAR(50) NOT NULL ,      " +
                    "email          VARCHAR(30) NOT NULL , 	    " +
                    "age            NUMBER(4) ,                 " +
                    "regularity     NUMBER(4)                  " +
                    ")";

    public static final String ROUTE_CREATE_TABLE =
            "CREATE TABLE Route (							  			    " +
                    "id 		 INTEGER PRIMARY KEY AUTOINCREMENT,             " +
                    "name        VARCHAR(200) NOT NULL ,	        		" +
                    "km          NUMBER(6) NOT NULL ,					    " +
                    "time        VARCHAR(12) NOT NULL ,		        	    " +
                    "reward      NUMBER(4) NOT NULL ,                       " +
                    "date    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  " +
                    "area        VARCHAR(200)       					    " +
                    ")";

    public static final String TRACK_CREATE_TABLE =
            "CREATE TABLE  Track(" +
                    "idRoute    INTEGER NOT NULL ,		                    " +
                    "nickname   VARCHAR(50) NOT NULL ,		                    " +
                    "FOREIGN KEY (nickname) REFERENCES User(nickname) ON DELETE CASCADE ,     " +
                    "FOREIGN KEY (idRoute) REFERENCES Route(id) ON DELETE CASCADE,           " +
                    "PRIMARY KEY (nickname,idRoute)                 " +
                    ")";

    public static final String ADD_TOTALKM=
    "ALTER TABLE User "+
            "ADD totalKm NUMBER(10) DEFAULT 0 NOT NULL ";

    public static final String ADD_TOTALCOINS=
            "ALTER TABLE User "+
                    "ADD  totalCoins NUMBER(10) DEFAULT 0 NOT NULL ";

    public static final String ADD_TOTALTRIPS=
            "ALTER TABLE User "+
                    "ADD totalTrips NUMBER(10) DEFAULT 0 NOT NULL ";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_CREATE_TABLE);
        db.execSQL(ROUTE_CREATE_TABLE);
        db.execSQL(TRACK_CREATE_TABLE);
        db.execSQL(ADD_TOTALKM);
        db.execSQL(ADD_TOTALCOINS);
        db.execSQL(ADD_TOTALTRIPS);
    }



     @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LoginDataBaseAdapter.class.getName(), "Upgrading database from version "
                    + oldVersion + " to " + newVersion + ",which will destroy all old data");
            for (int i=0;i<tables.length;i++){
                db.execSQL("DROP TABLE IF EXISTS " + tables[i] );
            }
            for (int i=0;i<triggers.length;i++){
                db.execSQL("DROP TRIGGER IF EXISTS " + triggers[i] );
            }
            onCreate(db);

    }

    public boolean validateUser(String username, String password){
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + "User" + " WHERE "
                        + "nickname" + "='" + username +"'AND "+"password"+"='"+password+"'" ,  null);
        if (c.getCount()>0)
            return true;
        return false;
    }

}