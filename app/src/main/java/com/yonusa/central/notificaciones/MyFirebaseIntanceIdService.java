package com.yonusa.central.notificaciones;

import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseIntanceIdService extends FirebaseCloudMessagingService {


    @Override
    public void onNewToken(String token) {
        // sending token to server here


    }
}
