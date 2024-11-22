package com.withsejong.chatting

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.chatting.chattingRoom.ChattingRoomActivity
import com.withsejong.databinding.FragmentChatBinding
import com.withsejong.home.HomeFragment
import com.withsejong.retrofit.LoadChattingRoomResponse
import com.withsejong.retrofit.RefreshTokenResponse
import com.withsejong.retrofit.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    lateinit var chattingAdapter: ChattingAdapter
    private var chattingRoom = ArrayList<LoadChattingRoomResponse>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        chattingRoom.clear()



        val tokenSharedPreferences = requireContext().getSharedPreferences("token",MODE_PRIVATE)
        val userInfoSharedPreferences = requireContext().getSharedPreferences("userInfo",MODE_PRIVATE)

        val accessToken = tokenSharedPreferences.getString("accessToken","").toString()
        val studentId = userInfoSharedPreferences.getString("studentId", "").toString()

        Log.d("ChatFragment_TAG1",accessToken)
        Log.d("ChatFragment_TAG2",studentId)

        val saveID = userInfoSharedPreferences.getString("studentId", "Error")
        val saveAccessToken = tokenSharedPreferences.getString("accessToken","Error")
        val saveRefreshToken = tokenSharedPreferences.getString("refreshToken", "Error")


        val chattingThread = Thread{
            //TODO 레트로핏 코드 작성 val response =

            val response = RetrofitClient.instance.loadChattingroom("Bearer $accessToken",studentId).execute()

            if(response.code()==403){
                //TODO 토큰 만료
                val jsonObject = JSONObject()
                jsonObject.put("studentId", saveID)
                jsonObject.put("accessToken", saveAccessToken)
                jsonObject.put("refreshToken", saveRefreshToken)

                RetrofitClient.instance.refreshToken(accessToken = "Bearer $saveAccessToken",
                    JsonParser.parseString(jsonObject.toString()))
                    .enqueue(object : Callback<RefreshTokenResponse> {
                        override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
                            if(response.isSuccessful){
                                Log.d("${tag}_TAG,갱신 전", saveAccessToken.toString())
                                val accessToken = response.body()?.accessToken
                                val refreshToken = response.body()?.refreshToken
                                val tokenSharedPreferencesEditor = requireContext().getSharedPreferences("token",
                                    Context.MODE_PRIVATE).edit()
                                tokenSharedPreferencesEditor.putString("accessToken",accessToken)
                                tokenSharedPreferencesEditor.putString("refreshToken", refreshToken)
                                tokenSharedPreferencesEditor.apply()
                                Log.d("${tag}_TAG,갱신 후", accessToken.toString())

                                //TODO 공지사항 다시 받는 코드 추가
                                reloadChatList(accessToken.toString(),studentId)
                            }
                        }

                        override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                            Log.d("${tag}_TAG", t.toString())
                        }

                    })
            }


            else if(response.isSuccessful){

                response.body()?.let { body ->
                    chattingRoom.addAll(body)
                }



                Log.d("ChatFragment_TAG3", response.toString())
                Log.d("ChatFragment_TAG4", response.body().toString())
                for(i:Int in 0 until chattingRoom.size){
                    val response = RetrofitClient.instance.loadLastChat("Bearer ${accessToken}",chattingRoom[i].roomId).execute()
                    if(response.code()==403){

                    }
                    else if(response.isSuccessful){
                        chattingRoom[i].lastmsg= response.body()?.message.toString()
                    }


                    Log.d("ChatFragment_TAG_test", chattingRoom.toString())
                }


                requireActivity().runOnUiThread {
                    chattingAdapter.notifyDataSetChanged()
                }
            }
            else{
                //TODO 통신 실패
            }
        }
        chattingThread.join()
        chattingThread.start()






        chattingAdapter = ChattingAdapter(chattingRoom)
        binding.rcvChattingList.adapter = chattingAdapter
        binding.rcvChattingList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
//        chattingAdapter = ChattingAdapter(chattingRoom)
//        binding.rcvChattingList.adapter = chattingAdapter
//        binding.rcvChattingList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)



        val intentChattingRoom = Intent(requireContext(),ChattingRoomActivity::class.java)

        chattingAdapter.setItemClickListener(object :ChattingAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                intentChattingRoom.putExtra("roomId", chattingRoom[position].roomId)
                intentChattingRoom.putExtra("boardTitle", chattingRoom[position].boardTitle)
                startActivity(intentChattingRoom)
            }
        })




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    private fun reloadChatList(accessToken:String,studentId:String){
         val reloadThread = Thread{
            val response = RetrofitClient.instance.loadChattingroom("Bearer ${accessToken}", studentId).execute()

            if(response.code()==403){
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(),"통신 오류", Toast.LENGTH_SHORT).show()
                }
            }
            else if(response.isSuccessful){
                response.body()?.let { body ->
                    chattingRoom.addAll(body)
                }



                Log.d("ChatFragment_TAG3", response.toString())
                Log.d("ChatFragment_TAG4", response.body().toString())



                requireActivity().runOnUiThread {
                    chattingAdapter.notifyDataSetChanged()
                }
            }
            else{
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(),"기타 오류", Toast.LENGTH_SHORT).show()
                }
            }
        }
        reloadThread.join()
        reloadThread.start()

    }




}