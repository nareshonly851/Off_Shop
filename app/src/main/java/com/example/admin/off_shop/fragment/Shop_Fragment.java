package com.example.admin.off_shop.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.off_shop.R;
import com.example.admin.off_shop.adapter.Shop_Adapter;
import com.example.admin.off_shop.adapter.extra.RecyclerTouchListener;
import com.example.admin.off_shop.network.APIService;
import com.example.admin.off_shop.network.ApiClient;
import com.example.admin.off_shop.network.model.Shop;
import com.example.admin.off_shop.network.model.Shopes;
import com.example.admin.off_shop.service.helper.ILocationConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 30-03-2018.
 */

public class Shop_Fragment extends Fragment {
    private View view;

    Unbinder unbinder;


    @BindView(R.id.shop_list)
    RecyclerView shop_list;
    private APIService apiService;
    List<Shop> shopList;
    private Shop_Adapter shop_adapter;

    private CompositeDisposable disposable = new CompositeDisposable();



    private static final int DEFAULT_ZOOM = 18;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shop_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        initViews();
        return view;
    }

    private void initViews()
    {
        shopList = new ArrayList<>();


        final FragmentActivity fragmentBelongActivity = getActivity();
        getAllShop();






    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }

    private void getAllShop() {


        final AlertDialog progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        progressDialog.show();
        apiService = ApiClient.getClient(getActivity()).create(APIService.class);


        disposable.add(
                apiService.getAllShop()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Shopes>() {
                            @Override
                            public void onSuccess(Shopes user) {


                                //  SharedPrefManager.getInstance(getActivity()).userLogin(user.getUser());

                                shopList = user.getShops();


                                setAdapter(shopList);
                               // insertMarkers(shopList);




                                progressDialog.dismiss();


                            }

                            @Override
                            public void onError(Throwable e) {
                          //      Log.e(TAG, "onError: " + e.getMessage());
//                             //   new CustomToast().Show_Toast(getActivity(), view,
//                                        "Your Email Id & password not match .");

                                progressDialog.dismiss();

                            }
                        }));

    }

    private void setAdapter(List<Shop> shopList1) {
        shop_adapter = new Shop_Adapter(getActivity(), shopList1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        shop_list.setLayoutManager(mLayoutManager);
        shop_list.setItemAnimator(new DefaultItemAnimator());
        // shop_list.addItemDecoration(new DividerItemDecoration(MapsActivity.this, LinearLayoutManager.VERTICAL, 16));
        shop_list.setAdapter(shop_adapter);

//        shop_list.addOnItemTouchListener(new RecyclerTouchListener(this,
//                shop_list, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, final int position)
//            {
//
////                LatLng dest = new LatLng(shopList.get(position).getLat(),shopList.get(position).getLong());
////
////
////
////                String url = getUrl(home, dest);
////                MapsActivity.FetchUrl FetchUrl = new MapsActivity.FetchUrl();
////
////                // Start downloading json data from Google Directions API
////                FetchUrl.execute(url);
////
////                goHome(dest);
//
//
//
//
//
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
    }

//    private void insertMarkers(List<Shop> list) {
//        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//        final FragmentActivity fragmentBelongActivity = getActivity();
//        FragmentManager fragmentManager = fragmentBelongActivity.getSupportFragmentManager();
//        Fragment rightFragment =  fragmentManager.findFragmentById(R.id.shop_framelayout);
//
//
//            for (int i = 0; i < list.size(); i++) {
//                final LatLng position = new LatLng(list.get(i).getLat(), list.get(i).getLong());
//                final MarkerOptions options = new MarkerOptions().position(position);
//                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.tshirt));
//
//
//
//
//
//
//
//
//
//                builder.include(position);
//
//        }
//
//
//
//    }

}