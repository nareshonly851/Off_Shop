package com.example.admin.off_shop.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.admin.off_shop.R;
import com.example.admin.off_shop.network.APIService;
import com.example.admin.off_shop.network.APIUrl;
import com.example.admin.off_shop.extra.CustomToast;
import com.example.admin.off_shop.extra.Utils;
import com.example.admin.off_shop.network.model.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 20-02-2018.
 */

public class SignUp_Fragment extends Fragment implements  View.OnClickListener
{
  private   View view ;
  private RadioGroup radioGroup;
  private EditText name,email,password;
  private Button signup_btn;
  private ConstraintLayout constraintLayout;
    private static Animation shakeAnimation;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.signup_fragment, container, false);

        initViews();
        return view;
    }

    private void initViews()
    {
        radioGroup= view.findViewById(R.id.gender_rsdio);
        name=view.findViewById(R.id.name_editText);
        email=view.findViewById(R.id.email_editText);
        password=view.findViewById(R.id.password_editText);
        signup_btn=view.findViewById(R.id.btn_sigmup);
        constraintLayout=view.findViewById(R.id.constraintLayout);
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);
        signup_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == signup_btn) {
            checkValidation();
        }
    }

    private void userSignIn()
    {
        final AlertDialog progressDialog  = new SpotsDialog(getActivity(), R.style.Custom);
        progressDialog.show();

        String email =this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();



        // find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());

        String name =this.name.getText().toString().trim();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);


        Call<Result> call = service.createUser(name,email,password,radioButton.getText().toString(),"");

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();



                if (!response.body().getError()) {

                    Toast.makeText(getActivity(), "data"+response.body().getUser().getEmail(), Toast.LENGTH_LONG).show();
                    //  finish();
                    // SharedPrefManager.getInstance(getActivity()).userLogin(response.body().getUser());
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void checkValidation() {
        // Get email id and password
        String getEmailId = email.getText().toString().trim();
        String getPassword = password.getText().toString().trim();
        String name_str = name.getText().toString().trim();


        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || name_str.equals("") || name_str.length() == 0) {
            constraintLayout.startAnimation(shakeAnimation);
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
