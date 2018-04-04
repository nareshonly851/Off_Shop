package com.example.admin.off_shop.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.off_shop.R;

import com.example.admin.off_shop.adapter.Shop_Adapter;
import com.example.admin.off_shop.adapter.extra.DividerItemDecoration;
import com.example.admin.off_shop.adapter.extra.RecyclerTouchListener;
import com.example.admin.off_shop.extra.CustomToast;
import com.example.admin.off_shop.extra.MyApplication;
import com.example.admin.off_shop.extra.SharedPrefManager;
import com.example.admin.off_shop.network.APIService;
import com.example.admin.off_shop.network.ApiClient;
import com.example.admin.off_shop.network.model.Result;
import com.example.admin.off_shop.network.model.Shop;
import com.example.admin.off_shop.network.model.Shopes;
import com.example.admin.off_shop.service.LocationService;
import com.example.admin.off_shop.service.NetworkChangeReceiver;
import com.example.admin.off_shop.service.helper.AppUtils;
import com.example.admin.off_shop.service.helper.DataParser;
import com.example.admin.off_shop.service.helper.ILocationConstants;
import com.github.kayvannj.permission_utils.Func2;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ILocationConstants, NetworkChangeReceiver.ConnectivityReceiverListener {
    private PermissionUtil.PermissionRequestObject mBothPermissionRequest;

    private GoogleMap mMap;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private List<Shop> shopList;

    NetworkChangeReceiver connectivityReceiver;


    private Location mLastKnownLocation;
    Marker marker;
    private static final int DEFAULT_ZOOM = 18;

    private CameraPosition mCameraPosition;
    private LocationReceiver locationReceiver;
    private APIService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    Unbinder unbinder;
    private Shop_Adapter shop_adapter;
    private LatLng home;


    @BindView(R.id.root)
    CoordinatorLayout root;

    @BindView(R.id.shop_list)
    RecyclerView shop_list;


    private SearchView searchView;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.map_laypout);
        unbinder = ButterKnife.bind(this);


        shopList = new ArrayList<>();
        getAllShop();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        //Receiver
        locationReceiver = new LocationReceiver();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_silver);
        mMap.setMapStyle(style);


    }

    private void startLocationService() {

        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);

    }


    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter(LOACTION_ACTION));


        /**
         * Runtime permissions are required on Android M and above to access User's location
         */
        if (AppUtils.hasM() && !(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

            askPermissions();

        } else {

            startLocationService();

        }

    }


    private class LocationReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {


            if (null != intent && intent.getAction().equals(LOACTION_ACTION)) {

                String locationData = intent.getStringExtra(LOCATION_MESSAGE);
                Double lat = intent.getDoubleExtra("lat", 0);
                Double log = intent.getDoubleExtra("log", 0);
                Toast.makeText(MapsActivity.this, "" + locationData, Toast.LENGTH_LONG).show();
                home = new LatLng(lat, log);
                if (marker != null) {
                    marker.remove();
                    marker = mMap.addMarker(new MarkerOptions().position(home).title("home"));


                    shop_list.removeAllViews();


                    setAdapter(sortLocations(shopList, lat, log));


                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(home).title("home"));
                    //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, DEFAULT_ZOOM));
                    setAdapter(sortLocations(shopList, lat, log));
                    goHome(home);


                }


                //  latlong_firebase(lat,log);


                //  Toast.makeText(Shayar_MapsActivity.this, lat.toString()+log.toString(),Toast.LENGTH_LONG).show();


            }

        }
    }



    //gps acess Permissions
    public void askPermissions() {
        mBothPermissionRequest =
                PermissionUtil.with(this).request(android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).onResult(
                        new Func2() {
                            @Override
                            protected void call(int requestCode, String[] permissions, int[] grantResults) {

                                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                                    startLocationService();

                                } else {

                                    Toast.makeText(MapsActivity.this, R.string.permission_denied, Toast.LENGTH_LONG).show();
                                }
                            }

                        }).ask(PERMISSION_ACCESS_LOCATION_CODE);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (null != mBothPermissionRequest) {
            mBothPermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // get all shop detaile method

    private void getAllShop() {


        final AlertDialog progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.show();
        apiService = ApiClient.getClient(this).create(APIService.class);


        disposable.add(
                apiService.getAllShop()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Shopes>() {
                            @Override
                            public void onSuccess(Shopes user) {


                                //  SharedPrefManager.getInstance(getActivity()).userLogin(user.getUser());

                                shopList = user.getShops();
                                Toast.makeText(MapsActivity.this, ""+shopList.size(), Toast.LENGTH_SHORT).show();
                              //  insertMarkers(shopList);


                                progressDialog.dismiss();


                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
//                             //   new CustomToast().Show_Toast(getActivity(), view,
//                                        "Your Email Id & password not match .");

                                progressDialog.dismiss();

                            }
                        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //list of near by shop

    public List<Shop> sortLocations(List<Shop> locations, final double myLatitude, final double myLongitude) {
        Comparator comp = new Comparator<Shop>() {
            @Override
            public int compare(Shop o, Shop o2) {
                float[] result1 = new float[3];
                android.location.Location.distanceBetween(myLatitude, myLongitude, o.getLat(), o.getLong(), result1);
                Float distance1 = result1[0];

                float[] result2 = new float[3];
                android.location.Location.distanceBetween(myLatitude, myLongitude, o2.getLat(), o2.getLong(), result2);
                Float distance2 = result2[0];

                return distance1.compareTo(distance2);
            }
        };


        Collections.sort(locations, comp);
        return locations;
    }

    // adapter of shop

    private void setAdapter(List<Shop> shopList1) {
        shop_adapter = new Shop_Adapter(MapsActivity.this, shopList1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        shop_list.setLayoutManager(mLayoutManager);
        shop_list.setItemAnimator(new DefaultItemAnimator());
        // shop_list.addItemDecoration(new DividerItemDecoration(MapsActivity.this, LinearLayoutManager.VERTICAL, 16));
        shop_list.setAdapter(shop_adapter);

        shop_list.addOnItemTouchListener(new RecyclerTouchListener(this,
                shop_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                LatLng dest = new LatLng(shopList.get(position).getLat(), shopList.get(position).getLong());


                String url = getUrl(home, dest);
                FetchUrl FetchUrl = new FetchUrl();

                // Start downloading json data from Google Directions API
                FetchUrl.execute(url);

                goHome(dest);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    // add list of marker i map
    private void insertMarkers(List<Shop> list) {
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < list.size(); i++) {
            final LatLng position = new LatLng(list.get(i).getLat(), list.get(i).getLong());
            final MarkerOptions options = new MarkerOptions().position(position);
            //          options.icon(BitmapDescriptorFactory.fromResource(R.drawable.tshirt));

            mMap.addMarker(options);


            builder.include(position);
        }

    }


    // draw lie

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }


    // get curret LatLng
    private void goHome(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                shop_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                shop_adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


    @OnClick(R.id.float_location)
    public void submit(View view) {
        goHome(home);
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
                    .make(root, message, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorGreen));


        } else {

            message = "Sorry! Not connected to internet";
            color = Color.RED;
            snackbar = Snackbar
                    .make(root, message, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorWhite));

        }


        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
    // Showing the status in Snackbar

    // Method to manually check connection status
    private boolean checkConnection() {
        boolean isConnected = NetworkChangeReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onStop() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
        unregisterReceiver(connectivityReceiver);
        super.onStop();
    }


}
