package com.example.bycycle.statTabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bycycle.R;
import com.example.bycycle.db.LoginDataBaseAdapter;
import com.example.bycycle.ui.MainScreen;

import org.w3c.dom.Text;

import java.sql.ResultSet;


public class Tab1Activity  extends Activity
{
    public String userName;
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userName=bundle.getString("username");

        double km = loginDataBaseAdapter.getUserData(userName,"totalKm");
        int trips = loginDataBaseAdapter.getUserData(userName,"totalTrips");

        double med = 0;
        if (trips>0){
            med = km/trips;
            med = Math.round(med * 100.0) / 100.0;
        }

        TextView textKm = (TextView)findViewById(R.id.TKm);
        TextView textTrips = (TextView)findViewById(R.id.NTrips);
        TextView textMed= (TextView)findViewById(R.id.Media);
        textKm.setText("Total distance:  "+km+" Km");
        textTrips.setText("Number of trips: "+trips);
        textMed.setText("Media km/trip: "+med);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                this.getParent().onKeyDown(keyCode,event);
        }
        return super.onKeyDown(keyCode, event);
    }
}