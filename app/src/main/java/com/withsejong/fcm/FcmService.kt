package com.withsejong.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService:FirebaseMessagingService() {

    //메시지를 수신할 때 호출
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


    }

//    override fun onNewToken(token: String)
//        super.onNewToken(token)
//        Log.d("FcmService", "Refreshed token = ${token}")
//    }


}