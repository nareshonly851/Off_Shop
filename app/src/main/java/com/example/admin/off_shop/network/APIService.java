package com.example.admin.off_shop.network;



import com.example.admin.off_shop.network.model.Result;
import com.example.admin.off_shop.network.model.Shop;
import com.example.admin.off_shop.network.model.Shopes;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Belal on 14/04/17.
 */

public interface APIService {

    //The register call
    @FormUrlEncoded
    @POST("register")
    Call<Result> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("") String profile);


//    //the signin call
//    @FormUrlEncoded
//    @POST("login")
//    Call<Result> userLogin(
//            @Field("email") String email,
//                @Field("password") String password
//    );


    @FormUrlEncoded
    @POST("login")
    Single<Result> userLogin( @Field("email") String email,
                          @Field("password") String password);


    @GET("shops")
    Single<Shopes> getAllShop();



}
