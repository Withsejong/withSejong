package com.withsejong.chatting.chattingRoom

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.input.key.Key.Companion.G
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.databinding.ActivityChattingRoomBinding
import com.withsejong.retrofit.FcmResponse
import com.withsejong.retrofit.LoadingChattingResponse
import com.withsejong.retrofit.RetrofitClient
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        val subscriber = intent.getStringExtra("subscriber")
        val publisher = intent.getStringExtra("publisher")
        val targetId =
            if(subscriber==studentId){
            publisher
        }
        else{
            subscriber
            }

        Log.d("ChattingRoomActivity_TAG", studentId.toString())
        binding.ibtnAdditionalFunction.setOnClickListener {
            showBottomSheetMenu(targetId!!,roomId,studentId)
        }

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

            val msg = binding.etMessageInput.text.toString()
            //TODO 서버로 메시지 전송 후
            if(msg.length>0){
                sendStomp(msg, roomId, studentId.toString())
                sendFcm(targetId.toString(),msg)
            }
        }
    }

    override fun onDestroy() {
        //TODO 소켓 disconnect
        super.onDestroy()
        disposeStomp()
    }
    private fun sendFcm(targetId:String, msg: String){
        val jsonObject = JSONObject()
        jsonObject.put("studentId", targetId)
        jsonObject.put("title","세종끼리")
        jsonObject.put("body",msg)

        RetrofitClient.instance.sendFcmNotification(accessToken = "Bearer $accessToken",JsonParser.parseString(jsonObject.toString())).enqueue(
            object : Callback<FcmResponse> {
                override fun onResponse(call: Call<FcmResponse>, response: Response<FcmResponse>) {
                    if(response.isSuccessful){
                        //Toast.makeText(this@ChattingRoomActivity, "알림발송 성공", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<FcmResponse>, t: Throwable) {
                    Toast.makeText(this@ChattingRoomActivity, "알림발송 살패", Toast.LENGTH_SHORT).show()

                }

            })
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
    // BottomSheetDialog 표시 메서드
    private fun showBottomSheetMenu(targetId:String,roomId:Int,studentId:String) {
        // BottomSheetDialog 생성
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.chatting_room_add_func_menu_bottomsheet_dialog_fragment, null)

        // 레이아웃에서 각 항목 연결 및 클릭 리스너 설정
        val menuLocation = bottomSheetView.findViewById<TextView>(R.id.menu_location)
        val menuDate = bottomSheetView.findViewById<TextView>(R.id.menu_date)

        menuLocation.setOnClickListener {
            // "장소 제안하기" 클릭 처리
            handleLocationSuggestion(targetId,roomId,studentId)
            bottomSheetDialog.dismiss() // 다이얼로그 닫기
        }

        menuDate.setOnClickListener {
            // "거래 날짜 정하기" 클릭 처리
            handleDateSetting(targetId,roomId,studentId)
            bottomSheetDialog.dismiss() // 다이얼로그 닫기
        }

        // BottomSheetDialog 설정 및 표시
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
    // "장소 제안하기" 처리 메서드
    private fun handleLocationSuggestion(targetId:String,roomId: Int,studentId:String) { //targetId는 fcm알림 용

        val placeBottomSheet = ChattingRoomPlaceBottomSheetDialogFragment { selectedPlace ->
            // 장소 선택 시 sendFcm 호출
            //sendFcm(targetId = targetId, msg = "선택된 장소: ${selectedPlace.name}")
            sendStomp(msg = "장소추천: ${selectedPlace.name}",roomId,studentId)
        }
        placeBottomSheet.show(supportFragmentManager, "ChattingRoomPlaceBottomSheetDialogFragment")

    }

    // "거래 날짜 정하기" 처리 메서드
    private fun handleDateSetting(targetId:String,roomId: Int,studentId:String) {//targetId는 fcm알림 용
        val tradeDatePicker = ChattingRoomTimeBottomSheetDialogFragment { selectedDate, selectedTime ->
            // 선택된 날짜와 시간을 처리
            //Toast.makeText(this, "시간추천: ${selectedDate}, ${selectedTime}", Toast.LENGTH_SHORT).show()
            sendStomp(msg = "시간추천: ${selectedDate}, ${selectedTime}",roomId,studentId)
        }
        tradeDatePicker.show(supportFragmentManager, "TradeDatePickerBottomSheet")
    }

}
