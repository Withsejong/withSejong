package com.withsejong.chatting.chattingRoom

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.input.key.Key.Companion.G
import androidx.recyclerview.widget.LinearLayoutManager
import com.withsejong.databinding.ActivityChattingRoomBinding
import com.withsejong.retrofit.LoadingChattingResponse
import com.withsejong.retrofit.RetrofitClient
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader


class ChattingRoomActivity : AppCompatActivity() {

    private var stompLifecycleDisposable: Disposable? = null

    private lateinit var binding: ActivityChattingRoomBinding
    private var pastChatting = ArrayList<LoadingChattingResponse>()
    private var stompClient: StompClient? = null
    private var headerList: List<StompHeader>? = null
    private var isUnexpectedClosed = false
    private lateinit var accessToken: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibtnBack.setOnClickListener {
            finish()
        }

        val roomId = intent.getIntExtra("roomId", -1)
        val boardTitle = intent.getStringExtra("boardTitle").toString()
        Log.d("ChattingRoomActivity_TAG", boardTitle.toString())
        binding.tvUpperBooknameIndicator.text = boardTitle


        val tokenSharedPreference = getSharedPreferences("token", MODE_PRIVATE)
        accessToken = tokenSharedPreference.getString("accessToken", "").toString()

        val userInfoSharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
        val studentId = userInfoSharedPreferences.getString("studentId", "").toString()
        Log.d("ChattingRoomActivity_TAG", studentId.toString())


        val loadPastChattingThread = Thread {
            val response =
                RetrofitClient.instance.loadChatting(roomId, "Bearer $accessToken").execute()

            if (response.code() == 403) {

            } else if (response.isSuccessful) {
//                response.body()?.let { body ->
//                    pastChatting.addAll(body)
//                }
                response.body()?.forEach {//1이면 남이쓴것 0이면 내가쓴것
                    Log.d("ChattingRoomActivity_TAG", "${it.sender}/${it.message}")

                    if (it.sender != studentId) {
                        pastChatting.add(
                            LoadingChattingResponse(
                                1,
                                it.roomId,

                                it.message,
                                it.sender,
                                it.createdAt,

                                )
                        )
                    } else {
                        pastChatting.add(
                            LoadingChattingResponse(
                                0,
                                it.roomId,
                                it.message,
                                it.sender,
                                it.createdAt,
                            )
                        )
                    }
                }

                Log.d("ChattingRoomActivity_TAG", pastChatting.toString())

                runOnUiThread {
                    scrollToBottom()
                    binding.rcvChattingList.adapter = ChattingRoomAdapter(pastChatting)
                    binding.rcvChattingList.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                }
            } else {

            }
        }
        loadPastChattingThread.join()
        loadPastChattingThread.start()
        initStomp(roomId, studentId.toString())


        binding.ivSendMsg.setOnClickListener {
            //TODO 서버로 메시지 전송 후
            sendStomp(binding.etMessageInput.text.toString(), roomId, studentId.toString())

        }


    }

    override fun onDestroy() {
        //TODO 소켓 disconnect
        super.onDestroy()
        disposeStomp()


    }


    private fun initStomp(roomId: Int, studentId: String) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://15.164.218.81:8080/ws/chat")

        stompLifecycleDisposable=stompClient?.lifecycle()?.subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> Log.d(TAG, "Stomp connection opened")
                LifecycleEvent.Type.ERROR -> {
                    Log.e(TAG, "Error", lifecycleEvent.exception)
                    if (lifecycleEvent.exception?.message?.contains("EOF") == true) {
                        isUnexpectedClosed = true
                    }
                }

                LifecycleEvent.Type.CLOSED -> {
                    Log.d(TAG, "Stomp connection closed")
                    if (isUnexpectedClosed) {
                        // EOF Error
                        initStomp(roomId, studentId)
                        isUnexpectedClosed = false
                    }
                }

                LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                    Log.e(TAG, "Stomp failed server heartbeat")
                }
            }

        }

        stompClient?.connect()


        stompLifecycleDisposable=stompClient?.topic("/sub/${roomId}")?.subscribe { topicMessage ->
            Log.d("message Receive", topicMessage.payload)
            val sender = JSONObject(topicMessage.payload).getString("sender")
            //val createdAt = JSONObject(topicMessage.payload).getString("createdAt")
            if (sender != studentId) {
                val msg = JSONObject(topicMessage.payload).getString("message")
                pastChatting.add(LoadingChattingResponse(1,roomId, msg, sender, "12:00"))

                runOnUiThread {

                    if(pastChatting.size!=0){
                        binding.rcvChattingList.adapter?.notifyItemInserted(pastChatting.size-1)
                        scrollToBottom()
                    }

                }
            }
        }
    }


    fun sendStomp(msg: String, roomId: Int, studentId: String) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("sender", studentId)
        data.put("message", msg)

        stompClient?.send("/pub/message", data.toString())?.subscribe()
        Log.d("Message Send", "내가 보낸거: " + msg)

        pastChatting.add(LoadingChattingResponse(0,roomId, msg, studentId, "12:00"))
        binding.rcvChattingList.adapter?.notifyItemInserted(pastChatting.size-1)
        scrollToBottom()
        binding.etMessageInput.setText("")



    }

    fun disposeStomp(){
        stompLifecycleDisposable?.dispose()
        stompLifecycleDisposable = null
    }

    private fun scrollToBottom() {
        binding.rcvChattingList.post {
            binding.rcvChattingList.adapter?.itemCount.takeIf { (it ?: 0) > 0 }?.let { itemCount ->
                binding.rcvChattingList.scrollToPosition(itemCount - 1)
            }
        }
    }



}
