package com.food.rescue.teamxero;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Class extending FirebaseInstanceIdService
 * Created by rishi on 9/24/16.
 */

public class NotificationInstanceIDService extends FirebaseInstanceIdService {

        private static final String TAG = "FirebaseIIDService";

        @Override
        public void onTokenRefresh() {

            //Getting registration token
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            //Displaying token on logcat
            Log.d(TAG, "Refreshed token: " + refreshedToken);

        }

        private void sendRegistrationToServer(String token) {
            //You can implement this method to store the token on your server
            //Not required for current project
        }
}


