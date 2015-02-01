package com.example.bycycle.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bycycle.R;
import com.example.bycycle.db.LoginDataBaseAdapter;


public class LogginActivity extends Activity {
    public LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loggin);


        // create a instance of SQLite Database
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Login --> Main Screen
	  public void handleLogin(View view) {
          Intent intent = new Intent();



          // get the Refferences of views
          EditText editTextUserName = (EditText) findViewById(R.id.et_nickname);
          EditText editTextPassword = (EditText) findViewById(R.id.et_password);


          switch (view.getId()) {
              case R.id.b_signin:
                    //Clear error messages
                  editTextUserName.setError(null);
                  editTextPassword.setError(null);
                  // get The User name and Password
                  String userName = editTextUserName.getText().toString();
                  String password = editTextPassword.getText().toString();
                  // check if any of the fields are vaccant
                  if(userName.equals("")||password.equals(""))
                  {
                      if (userName.equals("")){
                          String estring="Field vacant";
                          ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
                          SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                          ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                          editTextUserName.setError(ssbuilder);
                      }
                      if (password.equals("")){
                          String estring="Field vacant";
                          ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
                          SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                          ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                          editTextPassword.setError(ssbuilder);
                      }
                      return;
                  }

                  // fetch the Password form database for respective user name
                  boolean exists= loginDataBaseAdapter.getSingleEntry(userName,password);


                  // check if the Stored password matches with  Password entered by user
                  if (exists) {
                      Bundle bundle = new Bundle();
                      bundle.putString("username" , userName );
                      intent.setClass(this, MainScreen.class);
                      intent.putExtras( bundle );
                      startActivity(intent);
                      finish();
                  } else {
                      String estring="Credentials error";
                      ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
                      SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                      ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                      editTextPassword.setError(ssbuilder);


                  }

          }
      }

	// If the user isn't registered --> Register Screen
	  public void handleRegister(View view) {
		Intent intent = new Intent();
	    switch (view.getId()) {
	    case R.id.LinkToRegisterScreen:
	    	//Change to MainScreen
            intent.setClass(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
	  }



}

