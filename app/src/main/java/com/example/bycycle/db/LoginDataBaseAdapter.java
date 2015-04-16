package com.example.bycycle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.bycycle.routesList.RouteView;

import java.sql.ResultSet;

public class LoginDataBaseAdapter
{
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 5;
    public static final int NAME_COLUMN = 1;
    // Variable to hold the database instance
    public  SQLiteDatabase db;
    // Database open/upgrade helper
    private DatabaseHandler dbHelper;

    public  LoginDataBaseAdapter(Context context)
    {
        dbHelper = new DatabaseHandler(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  LoginDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();

        //Restart Database (uncomment it for restart and then comment it and install the app again)
        //dbHelper.onUpgrade(db,0,1);

        return this;
    }
    public void close()
    {
        dbHelper.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    /*
        Insert a new User in the database
     */

    public void insertUser(String userName,String password, String email, int age)
    {
        int regularity=1;
        int total=0;
        db = dbHelper.getWritableDatabase();


        db.execSQL(
                "INSERT INTO User (nickname, password, email, age, regularity, totalCoins) "
                        +" VALUES('"+userName+"','"+password+"','"+email+"',"+age+","+regularity+","+total+")");
        db.close();
    }

     /*
        Insert a new Route in the database and update user information
     */

    public void insertRoute(String nickname, String name,double numKm, String time, int coins, String area){
        area = "Tampere";
        db = dbHelper.getWritableDatabase();

        db.execSQL(
                "INSERT INTO Route (name, km, time, reward, area) "
                +" VALUES('"+name+"',"+numKm+",'"+time+"',"+coins+",'"+area+"')");

        String selectQuery = "SELECT id FROM Route";
        Cursor cursor = db.rawQuery(selectQuery, null);
        int id=0;
        if (cursor.moveToLast()) {
            id=cursor.getInt(0);
        }

        db.execSQL(
                "INSERT INTO Track (nickname, idRoute) "
                        +" VALUES('"+nickname+"',"+id+")");

        db.execSQL("UPDATE User SET totalKm = ( totalKm + "+numKm+" ) WHERE nickname = '"+nickname+"'");
        db.execSQL("UPDATE User SET totalCoins = ( totalCoins + "+coins+" ) WHERE nickname = '"+nickname+"'");
        db.execSQL("UPDATE User SET totalTrips = (totalTrips + 1) WHERE nickname = '"+nickname+"'");

        db.close();

    }

     /*
        Get user information
     */

    public int  getUserData(String nickname, String column){

        db = dbHelper.getReadableDatabase();

        String Table_Name="User";

        String selectQuery = "SELECT "+column+" FROM " + Table_Name+" WHERE nickname = '"+nickname+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        int data=0;
        if (cursor.moveToFirst()) {
            data = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return data;
    }

     /*
        Get routes of a user
     */

    public RouteView[]  getRoutes(String nickname){

        db = dbHelper.getReadableDatabase();
        int numRoutes=0;

        String CountQuery = "SELECT count(*) FROM Route, Track"+
                " WHERE Route.id = Track.idRoute and Track.nickname = '"+nickname+"'";
        Cursor cursorC = db.rawQuery(CountQuery, null);
        if (cursorC.moveToFirst()) {
            numRoutes=cursorC.getInt(0);
        }
        cursorC.close();

        String selectQuery = "SELECT name, km, time, reward, date, area FROM Route, Track"+
                " WHERE Route.id = Track.idRoute and Track.nickname = '"+nickname+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        RouteView[] rv= new RouteView [numRoutes];
        int i=0;
        if (cursor.moveToFirst()) {

            do {
                String name = cursor.getString(0);
                double km = cursor.getDouble(1);
                String time = cursor.getString(2);
                int reward = cursor.getInt(3);
                String date = cursor.getString(4).substring(0,10);
                String area = cursor.getString(5);
                rv[i]=new RouteView(name,area,km+" km - "+time,date,reward);
                i+=1;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return rv;
    }

    /*
       Delete a user
     */
    public int deleteEntry(String UserName)
    {
        //String id=String.valueOf(ID);
        String where="nickname=?";
        int numberOFEntriesDeleted= db.delete("User", where, new String[]{UserName}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    public boolean getSingleEntry(String userName,String password)
    {
        Cursor c = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + "User" + " WHERE "
                        + "nickname" + "='" + userName +"'AND "+"password"+"='"+password+"'" ,  null);
        if (c.getCount()>0)
            return true;
        return false;
    }

     /*
        Returns true if the name is not already used
     */

    public boolean namefree(String userName)
    {
        Cursor c = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + "User" + " WHERE "
                        + "nickname" + "='" + userName +"'",  null);
        if (c.getCount()>0)
            return false;
        return true;
    }

}