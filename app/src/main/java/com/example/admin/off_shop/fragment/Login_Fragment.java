package com.example.admin.off_shop.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.admin.off_shop.R;
import com.example.admin.off_shop.activity.Home_Activity;
import com.example.admin.off_shop.extra.SharedPrefManager;
import com.example.admin.off_shop.network.APIService;
import com.example.admin.off_shop.network.APIUrl;
import com.example.admin.off_shop.extra.CustomToast;
import com.example.admin.off_shop.extra.Utils;
import com.example.admin.off_shop.network.ApiClient;
import com.example.admin.off_shop.network.model.Result;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login_Fragment extends Fragment implements View.OnClickListener {
    private static View view;


    private static EditText emailid, pwd;
    private static Button loginButton, fb_button, gmail_button;
    private static TextView forgotPassword, signUp, login;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    private LoginButton facebook_login_button;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private APIService apiService;


    private static final String TAG = "Login_First_Fragment";

    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.login_fragment, container, false);

        initViews();

        return view;
    }


    // Initiate Views
    private void initViews() {


        emailid = view.findViewById(R.id.email_edit);
        pwd = view.findViewById(R.id.password_edit);
        loginButton = view.findViewById(R.id.btn_sigmup);
        signUp = view.findViewById(R.id.sign_txt);

        fb_button = view.findViewById(R.id.fb_button);
        facebook_login_button = view.findViewById(R.id.connectWithFbButton);
        loginLayout = view.findViewById(R.id.login_layout);
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);


        fb_button.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        signUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == loginButton) {
            checkValidation();
        }
        if (view == signUp) {
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new SignUp_Fragment(),
                            Utils.SignUp_Fragment).commit();
        }
        if (view == fb_button) {
            facebook_login();
        }
    }

    private void userSignIn() {


        final AlertDialog progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        progressDialog.show();

        String email = emailid.getText().toString().trim();
        String password = pwd.getText().toString().trim();

        Log.d("login", email + password);


        apiService = ApiClient.getClient(getActivity()).create(APIService.class);


        disposable.add(
                apiService.userLogin(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Result>() {
                            @Override
                            public void onSuccess(Result user) {


                                SharedPrefManager.getInstance(getActivity()).userLogin(user.getUser());


                                progressDialog.dismiss();


                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                                new CustomToast().Show_Toast(getActivity(), view,
                                        "Your Email Id & password not match .");

                                progressDialog.dismiss();

                            }
                        }));

    }

    //facebook login

    public void facebook_login() {
        callbackManager = CallbackManager.Factory.create();
        facebook_login_button.performClick();
        facebook_login_button.setReadPermissions("email", "public_profile");
        // If using in a fragment
        facebook_login_button.setFragment(this);
        final AlertDialog progressDialog = new SpotsDialog(getActivity(), R.style.Custom);


        facebook_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("onSuccess");
//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setMessage("Procesando datos...");
//                progressDialog.show();
                progressDialog.show();


                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                        if (bFacebookData != null) {
                            progressDialog.dismiss();
                            // user_register(bFacebookData);

                            Log.d(TAG, bFacebookData.getString("first_name") + bFacebookData.getString("last_name"));

                            Log.d(TAG, bFacebookData.getString("email") + bFacebookData.getString("gender"));

                            Log.d(TAG, bFacebookData.getString("birthday") + bFacebookData.getString("location"));

                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                progressDialog.dismiss();
                //  stopAnim();
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                progressDialog.dismiss();
                //   stopAnim();
                System.out.println("onError");
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // data get facebook
    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {

                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
        }
        return null;
    }


    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString().trim();
        String getPassword = pwd.getText().toString().trim();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter both credentials.");

        }
        // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
            // Else do login and do your stuff
        else {

            //Do Login.
            userSignIn();

        }

    }

}
