package com.example.admin.off_shop.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.admin.off_shop.R;
import com.example.admin.off_shop.extra.MyApplication;
import com.example.admin.off_shop.extra.SharedPrefManager;
import com.example.admin.off_shop.network.model.User;
import com.example.admin.off_shop.service.NetworkChangeReceiver;

public class Splash_Activity extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener{
    private static int SPLASH_TIME_OUT = 3200;
    VideoView videoHolder ;

    private static  RelativeLayout relativeLayout;
    private  ImageView imageView;
    NetworkChangeReceiver connectivityReceiver;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_);
        imageView = findViewById(R.id.circle_logo);

        relativeLayout = findViewById(R.id.relativeLayout);

       user = SharedPrefManager.getInstance(this).getUser();
        Toast.makeText(this, ""+user.getName(), Toast.LENGTH_SHORT).show();




        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        rotate.setDuration(3000);
        rotate.setRepeatCount(Animation.INFINITE);
        imageView.startAnimation(rotate);
        // check internet
        checkConnection();


    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        Snackbar snackbar;

        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
            snackbar = Snackbar
                    .make(relativeLayout, message, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorGreen));


            new Handler().postDelayed(new Runnable(){
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */




                if (user!=null)
                {
                    Intent mainIntent = new Intent(Splash_Activity.this,MapsActivity.class);
                    startActivity(mainIntent);
                    finish();

                }
                else
                    {
                        Intent mainIntent = new Intent(Splash_Activity.this,Welcome_Activity.class);
                        startActivity(mainIntent);
                        finish();

                }




                }
            }, SPLASH_TIME_OUT);


        } else {

            message = "Sorry! Not connected to internet";
            color = Color.RED;
            snackbar = Snackbar
                    .make(relativeLayout, message, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorWhite));

        }



        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
    // Showing the status in Snackbar

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = NetworkChangeReceiver.isConnected();
        showSnack(isConnected);
    }



    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

         connectivityReceiver = new NetworkChangeReceiver();
        registerReceiver(connectivityReceiver, intentFilter);

        /*register connection status listener*/
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onStop()
    {
        unregisterReceiver(connectivityReceiver);
        super.onStop();
    }

}
