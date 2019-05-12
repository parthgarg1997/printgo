package com.parthgarg.printgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;

public class MainActivity extends AppCompatActivity {

    final String TAG=MainActivity.class.getSimpleName();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i(TAG, userStateDetails.getUserState().toString());
                    switch (userStateDetails.getUserState()){
                        case SIGNED_IN:
                            Intent i = new Intent(MainActivity.this,print.class);
                            startActivity(i);
                            break;
                        case SIGNED_OUT:
                            showSignIn();
                            break;
                        default:
                            AWSMobileClient.getInstance().signOut();
                            showSignIn();
                            break;
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, e.toString());
                }
            });
        }

        private void showSignIn() {
            try {
                AWSMobileClient.getInstance().showSignIn(this,
                        SignInUIOptions.builder()
                                .nextActivity(print.class).build()
                );
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

    }



