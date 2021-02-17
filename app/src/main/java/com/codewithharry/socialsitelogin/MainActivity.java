package com.codewithharry.socialsitelogin;




import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private CircleImageView profile;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//

        callbackManager = CallbackManager.Factory.create();


        info = findViewById(R.id.info);
        loginButton = findViewById(R.id.login_button);
//        profile=findViewById(R.id.profile_image);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                info.setText("User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode , resultCode , data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker t=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken==null){
                info.setText("Sign Up");

            }
            else {
                loaduserProfile(currentAccessToken);
            }


        }
    };

    private void loaduserProfile(AccessToken newAccessToken){
        GraphRequest request= GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, com.facebook.GraphResponse response) {
                if (object != null) {
                    try {
                        String email = object.getString("email");
                        String id = object.getString("id");
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        info.setText("Name: "+first_name +" "+last_name+"\nEmail: "+email);
                        System.out.println("\n set text \n");
                        String image_url="https://graph.facebook.com/"+id+"/picture?type=normal";
                        RequestOptions r= new RequestOptions();
                        r.dontAnimate();
//                        Glide.with(MainActivity.this).load(image_url).into(profile);

//                        Picasso.get().load(image_url).into(profile);
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        Bundle parameters=new Bundle();
        parameters.putString("fields","email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();

    }
}