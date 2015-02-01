package com.example.bycycle.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bycycle.R;
import com.example.bycycle.db.LoginDataBaseAdapter;
import com.example.bycycle.tracking.TrackingCalculations;

/**
 * Main Menu of the app
 */
public class MainScreen extends ActionBarActivity {

    public String userName;
    public EditText name;
    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userName=bundle.getString("username");

        int coins = loginDataBaseAdapter.getUserData(userName,"totalCoins");
        TextView coinsText = (TextView) findViewById(R.id.ViewCoins);
        coinsText.setText(""+coins);

        TextView nickText = (TextView) findViewById(R.id.username);
        nickText.setText(userName);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected

            case R.id.action_about:
                Intent intent2 = new Intent(MainScreen.this, AboutActivity.class);
                startActivity(intent2);
                break;

            default:
                break;
        }

        return true;
    }



	// "OnClick track" of the button
	  public void track(View view) {
		Intent intent = new Intent();
	    switch (view.getId()) {
	    case R.id.b_track:
	    	//Show Dialog
            showMyDialog(1);
	    }
	  }

    // "OnClick Route" of the button
    public void route(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.b_routes:
                //Change to RouteList
                Bundle bundle = new Bundle();
                bundle.putString("username", userName);
                intent.setClass(MainScreen.this, ListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

        }
    }

    //"OnClick Stats" of the button
    public void stats(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.b_stats:
                //Change to Stats Screen
                Bundle bundle = new Bundle();
                bundle.putString("username", userName);
                intent.setClass(MainScreen.this, StatsActivity.class);
                intent.putExtras( bundle );
                startActivity(intent);
                finish();
        }
    }
    public void shop(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.b_shop:
                //Change to Shop Screen
                /*Bundle bundle = new Bundle();
                bundle.putString("username", userName);
                intent.setClass(MainScreen.this, StatsActivity.class);
                intent.putExtras( bundle );
                startActivity(intent);
                finish();*/
        }
    }

    public void showMyDialog(int id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final View prompt;
        if(id==0) {
            dialog.setMessage("Do you want to exit the app?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(
                            getApplicationContext()
                            , "Bye bye!"
                            , Toast.LENGTH_SHORT)
                            .show();

                    finish();
                }
            });

            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        else{
            LayoutInflater li = LayoutInflater.from(this);
            prompt = li.inflate(R.layout.dialog, null);
            dialog.setView(prompt);

            dialog
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            //String route=name.getText().toString();
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("username", userName);
                            name = (EditText) prompt.findViewById(R.id.name_route);
                            String routeName= name.getText().toString();
                            bundle.putString("name",routeName);
                            intent.setClass(MainScreen.this, GPSMain.class);
                            intent.putExtras( bundle );
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                    // Cancelamos el cuadro de dialogo
                            dialog.cancel();
                        }
                    });
        }
        dialog.show();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Press back key
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                showMyDialog(0);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }




}