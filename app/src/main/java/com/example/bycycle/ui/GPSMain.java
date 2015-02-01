package com.example.bycycle.ui;

import java.util.ArrayList;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;

import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bycycle.R;
import com.example.bycycle.db.LoginDataBaseAdapter;
import com.example.bycycle.tracking.TrackingCalculations;

/**
 * This class manage the map and the tracking part of the app. It is listening a broadcast
 * receiver which receives location from the Service "TrackingCalculations" and updates the map.
 */

public class GPSMain extends Activity {

    protected MapView map;
    protected ArrayList<GeoPoint> waypoints;
    public double totalKM;
    public int totalReward;
    protected MyLocationNewOverlay myLocationOverlay;
    protected RoadManager roadManager;
    protected int regularity_factor;
    public String userName;
    public String routeName;
    public PathOverlay myPath;
    public Location prevPoint;

    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userName=bundle.getString("username");
        routeName=bundle.getString("name");

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        regularity_factor=loginDataBaseAdapter.getUserData(userName,"regularity");

        /*
            Initialization of the MAP
        */

        this.map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);

        //Routing via road manager
        roadManager = new OSRMRoadManager();

        waypoints = new ArrayList<GeoPoint>();
        totalKM=0;
        myPath = new PathOverlay(Color.RED, this);

        /*
        Initialization of LocationOverlay and Compass
         */
        myLocationOverlay = new MyLocationNewOverlay(this, map);
        CompassOverlay mCompassOverlay = new CompassOverlay(this, map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(myLocationOverlay);
        map.getOverlays().add(mCompassOverlay);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation();
        map.postInvalidate();

        //Initialization of BroadcastManager and the Service

        intent = new Intent(this, TrackingCalculations.class);
        startService(intent);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(TrackingCalculations.LOC));

        Chronometer chronometer = (Chronometer)findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
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



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    /*
        Handler for received Intents for the "locationChanged" event
     */
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location loc= intent.getExtras().getParcelable("loc"); //Receive the location

            GeoPoint position = new GeoPoint (loc); //Transform the Location to Geopoint
            if (position != null) {
                waypoints.add(position);
                if(waypoints.size()>2){
                    totalKM=totalKM+prevPoint.distanceTo(loc);
                    totalKM = Math.round(totalKM*Math.pow(10,0))/Math.pow(10,0);
                }
                myPath.addPoint(position);
                prevPoint = loc;
                TextView changeKm = (TextView)findViewById(R.id.numKM);
                if(totalKM<=999){
                    changeKm.setText(totalKM+" m");
                }
                else{
                    double kmProv=totalKM/1000;
                    kmProv=Math.round(kmProv*Math.pow(10,1))/Math.pow(10,1);
                    changeKm.setText(kmProv+" Km");
                }
                map.getOverlays().add(myPath);
                map.invalidate();
            }
                }
    };


    /*
        Calculate the reward of the trip.
        ** Minimum distance: 500m.
        ** 50 coins / route
        ** 10 coins / 100 meters
     */
    public int reward(double km) {
        int coins = 0;
        if(km>499){
            coins = ((int)(km/100))*10* regularity_factor;
            coins+=50;
        }
        return coins;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Press back key
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                showMyDialog(1);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    // "handleFinish track" of the button
    public void handleFinish(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.b_finish:
                //Change to GPSMain Screen
                showMyDialog(0);
        }
    }

    /*
        Customized Dialogs
     */
    public void showMyDialog(int id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        if(id==0) {
            /*
                Dialog displayed when you press the "Finish" Button
             */
            dialog.setMessage("Do you want to finish your trip?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(GPSMain.this, TrackingCalculations.class);
                    stopService(intent);

                    /*
                        Chronometer
                     */
                    Chronometer chrono = (Chronometer)findViewById(R.id.chronometer);

                    //total ms
                    long totTime = SystemClock.elapsedRealtime()-chrono.getBase();

                    //Time String xx:xx:xx
                    String value = chrono.getText().toString();

                    chrono.stop();

                    //Total seconds
                    int seconds =getTime(value);

                    Bundle bundle = new Bundle();
                    bundle.putString("username" , userName );

                    /*
                    ANIMATION
                     */
                    ViewStub stub = (ViewStub) findViewById(R.id.viewStub);
                    stub.setAlpha(50);
                    View inflatedView = stub.inflate();

                    //Animation Coin
                    ImageView img = (ImageView) inflatedView.findViewById(R.id.imageCoin);
                    Animation td =
                            AnimationUtils.loadAnimation(GPSMain.this, R.anim.bounce);
                    /*
                        Sound
                     */
                    //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.coinsound);
                    //mp.start();
                    td.setRepeatMode(Animation.RESTART);
                    td.setRepeatCount(0);
                    img.startAnimation(td);


                    /*
                        UPDATE REWARD INFORMATION
                     */

                    //Text Km tracked
                    TextView kmV =(TextView)inflatedView.findViewById(R.id.KmTracked);
                    double KmTracked = totalKM/1000;
                    KmTracked = Math.round(KmTracked * 100.0) / 100.0;
                    kmV.setText(KmTracked+" km");

                    //0.789

                    //Text reward
                    TextView rewardV =(TextView)inflatedView.findViewById(R.id.textCoins);
                    totalReward=reward(totalKM);
                    rewardV.setText(totalReward+" coins");

                    /*
                        Store the route in the database
                     */
                    loginDataBaseAdapter.insertRoute(userName,routeName,KmTracked,value,totalReward,"");

                    Button button = (Button) inflatedView.findViewById(R.id.buttonmenu);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("username", userName);
                            intent.setClass(GPSMain.this, MainScreen.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });

            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Continue!", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });
        }
        else{
            /*
                Dialog displayed when you press the "Back Key"
             */
            dialog.setMessage("Do you want to return to the Main Menu without saving?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(GPSMain.this, TrackingCalculations.class);
                    stopService(intent);
                    intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("username" , userName );
                    intent.setClass(GPSMain.this, MainScreen.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            });

            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Continue!", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });
        }
        dialog.show();
    }

    /*
    Transform String from Chronometer to Seconds
     */
    public static int getTime(String value){

        String [] parts = value.split(":");

        // Wrong format, no value for you.
        if(parts.length < 2 || parts.length > 3)
            return 0;

        int seconds = 0, minutes = 0, hours = 0;

        if(parts.length == 2){
            seconds = Integer.parseInt(parts[1]);
            minutes = Integer.parseInt(parts[0]);
        }
        else if(parts.length == 3){
            seconds = Integer.parseInt(parts[2]);
            minutes = Integer.parseInt(parts[1]);
            hours = Integer.parseInt(parts[1]);
        }

        return seconds + (minutes*60) + (hours*3600);
    }
}
