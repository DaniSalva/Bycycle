package com.example.bycycle.tracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.bycycle.R;
import com.example.bycycle.ui.MainScreen;

/**
 * This class manage the Reward Screen. It displays a animated coin and shows the number of
 * coins and kilometers that the user have tracked.
 */

public class RewardActivity extends Activity {
    public String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userName=bundle.getString("username");

        ImageView img = (ImageView) findViewById(R.id.imageCoin);
        Animation td =
                AnimationUtils.loadAnimation(this, R.anim.bounce);
        td.setRepeatMode(Animation.RESTART);
        td.setRepeatCount(0);
        img.startAnimation(td);
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	   /* switch(item.getItemId())
	    {
	        case R.id.action_settings:
	            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();;
	            break;
	        case R.id.action_search:
	            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
	            break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	 */
        return true;
    }

    // "OnClick track" of the button
    public void backtomenu(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.buttonmenu:
                //Change to GPSMain Screen
                Bundle bundle = new Bundle();
                bundle.putString("username", userName);
                intent.setClass(RewardActivity.this, MainScreen.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
        }
    }
}
