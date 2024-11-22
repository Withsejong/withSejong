package com.withsejong.stomp

import android.util.Log
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader


class StompClient{


    val url = "ws://15.164.163.65:8080/ws"
    val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

    fun runStomp(){

        stompClient.topic("/sub/message").subscribe { topicMessage ->
            Log.i("message Recieve", topicMessage.payload)
        }

        val headerList = arrayListOf<StompHeader>()
        headerList.add(StompHeader("inviteCode","test0912"))
        headerList.add(StompHeader("positionType", "1"))
        stompClient.connect(headerList)
        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.i("OPEND", "!!")
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.i("CLOSED", "!!")

                }
                LifecycleEvent.Type.ERROR -> {
                    Log.i("ERROR", "!!")
                    Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
                }
                else ->{
                    Log.i("ELSE", lifecycleEvent.message)
                }
            }
        }


        val data = JSONObject()
        data.put("positionType", "1")
        data.put("content", "test")
        data.put("messageType", "CHAT")
        data.put("destRoomCode", "test0912")

        stompClient.send("/stream/chat/send", data.toString()).subscribe()
    }



}