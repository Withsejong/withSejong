package com.withsejong.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    private val TAG = "FcmService"

    override fun onNewToken(token: String){
        super.onNewToken(token)
        Log.d("FcmService", "Refreshed token = ${token}")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG,"fcm message = ${message.data}")
    }

}