package com.example.admin.off_shop.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.off_shop.R;
import com.example.admin.off_shop.adapter.SliderAdapter;
import com.example.admin.off_shop.extra.SharedPrefManager;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome_Activity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotlayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mdots;
    private int currentpage = 0;
    private Button btn_pre, btn_next, get_login;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 2500;
    private SharedPrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);


        prefManager = new SharedPrefManager(this);

        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();

        }



        sliderAdapter = new SliderAdapter(this);


        viewPager = (ViewPager) findViewById(R.id.viewPager_slide);
        dotlayout = (LinearLayout) findViewById(R.id.dot_layout);
        get_login = (Button) findViewById(R.id.get_login);


        get_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchHomeScreen();

            }
        });

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentpage == 3) {
                    currentpage = 0;
                }
                viewPager.setCurrentItem(currentpage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


        viewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(listener);


    }

    private void launchHomeScreen()
    {
        prefManager.setFirstTimeLaunch(false);

        startActivity(new Intent(Welcome_Activity.this, Login_Activity.class));
        finish();
    }

    public void addDotsIndicator(int position) {
        mdots = new TextView[3];
        dotlayout.removeAllViews();

        for (int i = 0; i < mdots.length; i++) {
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.colorTranWhite));
            dotlayout.addView(mdots[i]);


        }


        if (mdots.length > 0) {
            mdots[position].setTextColor(getResources().getColor(R.color.colorLogin));

        }


    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentpage = position;
//            if (position==0)
//            {
//                btn_pre.setEnabled(false);
//                btn_next.setEnabled(true);
//                btn_pre.setVisibility(View.INVISIBLE);
//
//                btn_next.setText("Next");
//                btn_pre.setText("");
//
//            }else if (position==mdots.length-1)
//            {
//                btn_pre.setEnabled(true);
//                btn_next.setEnabled(true);
//                btn_pre.setVisibility(View.VISIBLE);
//
//                btn_next.setText("Finish");
//                btn_pre.setText("Back");
//            }
//            else {
//                btn_pre.setEnabled(true);
//                btn_next.setEnabled(true);
//                btn_pre.setVisibility(View.VISIBLE);
//
//                btn_next.setText("Next");
//                btn_pre.setText("Back");
//
//            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
