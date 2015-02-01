package com.example.bycycle.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bycycle.R;
import com.example.bycycle.db.LoginDataBaseAdapter;
import com.example.bycycle.statTabs.Tab1Activity;
import com.example.bycycle.statTabs.Tab2Activity;
import com.example.bycycle.statTabs.Tab3Activity;

/**
 * This class manage the stats of the user.
 */
public class StatsActivity extends TabActivity {

    public String userName;
    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userName=bundle.getString("username");

        int age = loginDataBaseAdapter.getUserData(userName,"age");
        int regularity = loginDataBaseAdapter.getUserData(userName,"regularity");

        /*
            Get User Information
         */
        TextView textUser = (TextView)findViewById(R.id.NameStats);
        textUser.setText("Name : "+userName);

        TextView textAge = (TextView)findViewById(R.id.AgeStats);
        textAge.setText("Age: "+age);

        TextView textReg = (TextView)findViewById(R.id.RegularityStats);
        textReg.setText("Regularity: "+regularity);

        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        //tab1.setContent(new Intent(this, Tab1Activity.class));

        Bundle bundle2 = new Bundle();
        bundle2.putString("username", userName);

        tab1.setIndicator("Total");
        Intent updatesIntent = new Intent(this, Tab1Activity.class);
        updatesIntent.putExtras(bundle2);
        tab1.setContent(updatesIntent);

        bundle2 = new Bundle();
        bundle2.putString("username", userName);

        tab2.setIndicator("Daily");
        updatesIntent = new Intent(this, Tab2Activity.class);
        updatesIntent.putExtras(bundle2);
        tab2.setContent(updatesIntent);

        bundle2 = new Bundle();
        bundle2.putString("username", userName);


        tab3.setIndicator("Monthly");
        updatesIntent = new Intent(this, Tab3Activity.class);
        updatesIntent.putExtras(bundle2);
        tab3.setContent(updatesIntent);

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("username", userName);
                intent.setClass(StatsActivity.this, MainScreen.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


