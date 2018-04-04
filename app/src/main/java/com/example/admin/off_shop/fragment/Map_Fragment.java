package com.example.admin.off_shop.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.admin.off_shop.R;
import com.example.admin.off_shop.activity.MapsActivity;
import com.example.admin.off_shop.adapter.Shop_Adapter;
import com.example.admin.off_shop.adapter.extra.RecyclerTouchListener;
import com.example.admin.off_shop.network.model.Shop;
import com.example.admin.off_shop.service.LocationService;
import com.example.admin.off_shop.service.helper.AppUtils;
import com.example.admin.off_shop.service.helper.ILocationConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 30-03-2018.
 */

public class Map_Fragment extends SupportMapFragment  implements OnMapReadyCallback, ILocationConstants {
    private View view;

    Unbinder unbinder;
    public GoogleMap mMap;
    Marker marker;
    private LatLng home;
    private LocationReceiver locationReceiver;
    private static final int DEFAULT_ZOOM = 18;

    MapView mMapView;
    private Shop_Adapter shop_adapter;

    public static Map_Fragment newInstance() {
        Map_Fragment fragment = new Map_Fragment();
        return fragment;
    }






    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.map_fragment, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        unbinder = ButterKnife.bind(this, view);

        initViews();
        return view;
    }

    private void initViews()
    {



        locationReceiver = new LocationReceiver();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_silver);
        mMap.setMapStyle(style);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }

    private void startLocationService() {

        Intent serviceIntent = new Intent(getActivity(), LocationService.class);
        getActivity().startService(serviceIntent);

    }
    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(locationReceiver, new IntentFilter(LOACTION_ACTION));

//
//        /**
//         * Runtime permissions are required on Android M and above to access User's location
//         */
//        if (AppUtils.hasM() && !(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
//
//            askPermissions();
//
//        } else {

            startLocationService();

     //   }

    }





    private class LocationReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {


            if (null != intent && intent.getAction().equals(LOACTION_ACTION)) {

                String locationData = intent.getStringExtra(LOCATION_MESSAGE);
                Double lat = intent.getDoubleExtra("lat", 0);
                Double log = intent.getDoubleExtra("log", 0);
               Toast.makeText(getActivity(), "" + locationData, Toast.LENGTH_LONG).show();
                home = new LatLng(lat, log);
                if (marker != null) {
                    marker.remove();
                    marker = mMap.addMarker(new MarkerOptions().position(home).title("home"));





                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(home).title("home"));

                   goHome(home);





                }





            }

        }
    }
    private void goHome(LatLng latLng)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);


    }



    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(locationReceiver);
    }




}
