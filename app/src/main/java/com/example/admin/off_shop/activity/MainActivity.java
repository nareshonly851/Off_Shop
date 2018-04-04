package com.example.admin.off_shop.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.admin.off_shop.R;
import com.example.admin.off_shop.extra.Utils;
import com.example.admin.off_shop.fragment.Login_Fragment;
import com.example.admin.off_shop.fragment.Map_Fragment;
import com.example.admin.off_shop.fragment.Shop_Fragment;

public class MainActivity extends FragmentActivity {
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null)
        {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.map_framelayout, new Map_Fragment(),
                            Utils.map).commit();

//            fragmentManager
//                    .beginTransaction()
//                    .replace(R.id.shop_framelayout, new Shop_Fragment(),
//                            Utils.shop).commit();
        }
    }
}
