package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class RenewActivity extends AppCompatActivity {
    private static final String TAG = "RenewActivity";
    TextView tex1,tex2,tex3;
    private static final String text1= "text01";
    private static final String text2= "text02";
    private static final String text3= "text03";
    private FirebaseRemoteConfig firebaseRemoteConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renewactivity);
        tex1= findViewById(R.id.price1);
        tex2 = findViewById(R.id.price2);
        tex3 = findViewById(R.id.price3);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // Create a Remote Config Setting to enable developer mode,
        FirebaseRemoteConfigSettings.Builder configBuilder = new FirebaseRemoteConfigSettings.Builder();
        // Sets the minimum interval between successive fetch calls.
        /**
         * For developer mode I'm setting 0 (zero) second
         * The default mode is 12 Hours. So for production mode it will be 12 hours
         */
        if (BuildConfig.DEBUG) {
            long cacheInterval = 0;
            configBuilder.setMinimumFetchIntervalInSeconds(cacheInterval);
        }
        // finally build config settings and sets to Remote Config
        firebaseRemoteConfig.setConfigSettingsAsync(configBuilder.build());
        /**
         *  Set default Remote Config parameter values. An app uses the in-app default values, and
         * when you need to adjust those defaults, you set an updated value for only the values you
         * want to change in the Firebase console
         */
        //set default values
        firebaseRemoteConfig.setDefaults(R.xml.firebase_defaults);
        fetchRemoteTitle();
    }
    private void fetchRemoteTitle() {
        // set text from remote
        tex1.setText(firebaseRemoteConfig.getString(text1));
        tex2.setText(firebaseRemoteConfig.getString(text2));
        tex3.setText(firebaseRemoteConfig.getString(text3));
        // [START fetch_config_with_callback]
        // override default value from Remote Config
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);
                            Toast.makeText(RenewActivity.this, "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RenewActivity.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
