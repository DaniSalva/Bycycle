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

/**
 * This class manage the registration of a new user.
 */
public class RegisterActivity extends Activity {

    EditText editTextUserName,editTextEmail,editTextPassword,editTextConfirmPassword, editTextAge;
    LoginDataBaseAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
        // get Instance  of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
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
	
	// If the user is already registered --> Login Screen
	  public void handleLogin(View view) {
		Intent intent = new Intent();
	    switch (view.getId()) {
	    case R.id.b_LinkToLoginScreen:
	    	//Change to LoginScreen
            intent.setClass(this, LogginActivity.class);
            startActivity(intent);
            finish(); 
	    }
	  }
	  
	//Register user and go to Main Screen
		  public void handleRegister(View view) {
			Intent intent = new Intent();
            editTextUserName=(EditText)findViewById(R.id.registerNickname);
            editTextEmail=(EditText)findViewById(R.id.registerEmail);
            editTextPassword=(EditText)findViewById(R.id.registerPassword);
            editTextConfirmPassword=(EditText)findViewById(R.id.ConfirmPassword);
            editTextAge=(EditText)findViewById(R.id.Age);

            switch (view.getId()) {
		    case R.id.b_Register:
                //Clear error messages
                editTextUserName.setError(null);
                editTextEmail.setError(null);
                editTextPassword.setError(null);
                editTextConfirmPassword.setError(null);

                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();
                String email= editTextEmail.getText().toString();
                String confirmPassword=editTextConfirmPassword.getText().toString();
                String age = editTextAge.getText().toString();
                int ageInt=0;
                if(!age.equals("")){
                    ageInt = Integer.parseInt(age);
                }

                // check if any of the fields are vaccant
                if(userName.equals("")||password.equals("")||email.equals("")||confirmPassword.equals(""))
                {
                    if(userName.equals("")){
                        String estring="Field vacant";
                        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
                        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                        editTextUserName.setError(ssbuilder);
                    }
                    if(password.equals("")){
                        String estring="Field vacant";
                        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
                        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                        editTextPassword.setError(ssbuilder);
                    }
                    if(email.equals("")){
                        String estring="Field vacant";
                        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
                        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                        editTextEmail.setError(ssbuilder);

                    }
                    if(confirmPassword.equals("")){
                        String estring="Field vacant";
                        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
                        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                        editTextConfirmPassword.setError(ssbuilder);
                    }
                    return;
                }
                // check if both password matches
                if(!password.equals(confirmPassword))
                {
                    String estring="Password doesn't match";
                    ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
                    SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                    ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                    editTextConfirmPassword.setError(ssbuilder);
                    return;
                }
                else if (!loginDataBaseAdapter.namefree(userName))
                {
                    String estring="Name is already in use";
                    ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
                    SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                    ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                    editTextConfirmPassword.setError(ssbuilder);
                    return;
                }
                else
                {
                    // Save the Data in Database
                    loginDataBaseAdapter.insertUser(userName, password,email,ageInt);
                    //Change to MainScreen
                    intent.setClass(this, LogginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }


		  }
}
