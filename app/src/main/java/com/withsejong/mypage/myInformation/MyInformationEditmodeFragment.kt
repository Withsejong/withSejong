package com.withsejong.mypage.myInformation

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
import androidx.activity.OnBackPressedCallback
import androidx.compose.animation.core.exponentialDecay
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.databinding.FragmentMyInformationEditmodeBinding
import com.withsejong.mypage.faq.FaqAdapter
import com.withsejong.retrofit.LoadFaqResponse
import com.withsejong.retrofit.LogoutResponse
import com.withsejong.retrofit.RefreshTokenResponse
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.UpdateUserInfoResponse
import com.withsejong.start.LoginChoicePage
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header


class MyInformationEditmodeFragment : Fragment() {

    private lateinit var binding : FragmentMyInformationEditmodeBinding

    private val TAG = "MyInformationEditmodeFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyInformationEditmodeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userInfoSharedPreferences = requireActivity().getSharedPreferences("userInfo",MODE_PRIVATE)
        val tokenSharedPreferences = requireContext().getSharedPreferences("token", MODE_PRIVATE)
        val saveId = userInfoSharedPreferences.getString("studentId", "error").toString()
        var saveNickname = userInfoSharedPreferences.getString("nickname","error")
        var saveMajor = userInfoSharedPreferences.getString("major","error")
        binding.tvNicknameIndicator.text = "${saveNickname}님"
        binding.etNicknameIndicator.setText("")
        binding.etMajorIndicator.setText("")
        binding.etNicknameIndicator.hint = "$saveNickname"
        binding.etMajorIndicator.hint = "$saveMajor"

        //이전 페이지 fragment 객체 정의
        val myInformationFragment = MyInformationFragment()


        binding.ibtnBack.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fcv_all_fragment,myInformationFragment).commit()
        }
        //휴대폰 뒤로가기를 누른 경우 이전 fragment로 돌아가는 행동 정의
        val backActionCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragmentManager = parentFragmentManager.beginTransaction()
                fragmentManager.replace(R.id.fcv_all_fragment,myInformationFragment).commit()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),backActionCallback)


        binding.tvSave.setOnClickListener {
            val updateInformation = userInfoSharedPreferences.edit()
            if(binding.etNicknameIndicator.text.length>0){
                updateInformation.putString("nickname",binding.etNicknameIndicator.text.toString())
                updateInformation.apply()
            }
            if(binding.etMajorIndicator.text.length>0){
                updateInformation.putString("major",binding.etMajorIndicator.text.toString())
                updateInformation.apply()
            }
            //TODO 나의 정보 업데이트 하는 api 연결
            val jsonObject = JSONObject()
            jsonObject.put("studentId", userInfoSharedPreferences.getString("studentId", "Error"))
            jsonObject.put("nickname", userInfoSharedPreferences.getString("nickname", "error"))
            jsonObject.put("major", userInfoSharedPreferences.getString("major","error"))

            val updateUserInfoThread=Thread{
                val response = RetrofitClient.instance.updateUserInfo("Bearer ${tokenSharedPreferences.getString("accessToken","error")}"
                    ,JsonParser.parseString(jsonObject.toString())).execute()

                Log.d(TAG, response.toString())

                if(response.code()==403){
                    refreshToken(saveId = saveId)
                    var accessToken = tokenSharedPreferences.getString("accessToken", "error").toString()
                    var refreshToken = tokenSharedPreferences.getString("refreshToken", "error").toString()
                    val jsonObject =JSONObject()
//                    jsonObject.put("accessToken", accessToken)
//                    jsonObject.put("refreshToken", refreshToken)
//                    jsonObject.put("studentId", saveId)

                    jsonObject.put("studentId", userInfoSharedPreferences.getString("studentId", "Error"))
                    jsonObject.put("nickname", userInfoSharedPreferences.getString("nickname", "error"))
                    jsonObject.put("major", userInfoSharedPreferences.getString("major","error"))

                    retryUpdateUserInfo(accessToken = accessToken,jsonObject,myInformationFragment)
                }
                else if(response.isSuccessful){
                    val fragmentManager = parentFragmentManager.beginTransaction()
                    fragmentManager.replace(R.id.fcv_all_fragment,myInformationFragment).commit()
                }
                else{
                    Log.d("MyInformationEditmodeFragment_TAG",response.toString())

                }
            }
            updateUserInfoThread.start()
            updateUserInfoThread.join()


//비동기 통신 주석처리
//            RetrofitClient.instance.updateUserInfo(
//                accessToken = "Bearer ${tokenSharedPreferences.getString("accessToken","error")}"
//                ,JsonParser.parseString(jsonObject.toString()))
//                .enqueue(object :  Callback<UpdateUserInfoResponse> {
//                    override fun onResponse(
//                        call: Call<UpdateUserInfoResponse>,
//                        response: Response<UpdateUserInfoResponse>
//                    ) {
//                        /*if(response.code()==403){ //토큰 만료인 경우
//                            refreshToken(saveId = saveId)
//                            val accessToken = tokenSharedPreferences.getString("accessToken", "error").toString()
//                            val refreshToken = tokenSharedPreferences.getString("refreshToken", "error")
//                            val jsonObject =JSONObject()
//                            jsonObject.put("accessToken", accessToken)
//                            jsonObject.put("refreshToken", refreshToken)
//                            jsonObject.put("studentId", saveId)
//                            refreshToken(saveId)
//                            retryUpdateUserInfo(accessToken = accessToken,jsonObject,myInformationFragment)
//                        }
//                        else*/ if(response.isSuccessful){
//                            val fragmentManager = parentFragmentManager.beginTransaction()
//                            fragmentManager.replace(R.id.fcv_all_fragment,myInformationFragment).commit()
//                        }
//
//                        else{
//                            Log.d("MyInformationEditmodeFragment_TAG",response.toString())
//                            Toast.makeText(requireContext(),"서버 통신 오류",Toast.LENGTH_SHORT).show()
//                        }
//                        Log.d("MyInformationEditmodeFragment_TAG_",response.toString())
//                    }
//
//                    override fun onFailure(call: Call<UpdateUserInfoResponse>, t: Throwable) {
//                        Log.d("MyInformationEditmodeFragment_TAG", t.toString())
//                    }
//                })
        }

        val intentLogout = Intent(requireActivity(), LoginChoicePage::class.java)
        binding.btnLogout.setOnClickListener {
            val saveId = userInfoSharedPreferences.getString("studentId","error").toString()
            var accessToken = tokenSharedPreferences.getString("accessToken","error").toString()
            RetrofitClient.instance.logout(accessToken = "Bearer $accessToken",studentId = saveId).enqueue(object : Callback<LogoutResponse>{
                override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                    Log.d(TAG,response.toString())
                    if(response.isSuccessful){
                        //Snackbar.make(requireView(),"로그아웃이 정상적으로 처리되었습니다.",Snackbar.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(),"로그아웃이 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(intentLogout)
                    }
                    else if(response.code()==403){//토큰 만료
                        //TODO 토큰 재발급 로직 필요
                        refreshToken(saveId)
                        accessToken = tokenSharedPreferences.getString("accessToken", "Error").toString()

                        retryLogout(accessToken,saveId,intentLogout)





                    }
                }
                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    Log.d(TAG,t.toString())
                }
            })
        }
    }



    private fun refreshToken(saveId:String){
        val tokenSharedPreferences = requireContext().getSharedPreferences("token",Context.MODE_PRIVATE)
        val accessToken = tokenSharedPreferences.getString("accessToken", "Error").toString()
        val refreshToken = tokenSharedPreferences.getString("refreshToken", "Error").toString()
        val jsonObject = JSONObject()
        jsonObject.put("accessToken", accessToken)
        jsonObject.put("refreshToken", refreshToken)
        jsonObject.put("studentId", saveId)


        val refreshTokenThread = Thread{
            val response = RetrofitClient.instance.refreshToken(accessToken = "Bearer $accessToken",
                JsonParser.parseString(jsonObject.toString())).execute()
            if(response.isSuccessful){
                Log.d("MyInformationEditmodeFragment_TAG_fun_refreshToken",response.toString())
                val tokenSharedPreferencesEditor = requireActivity().getSharedPreferences("token",Context.MODE_PRIVATE).edit()
                tokenSharedPreferencesEditor.putString("accessToken", response.body()?.accessToken)
                tokenSharedPreferencesEditor.putString("refreshToken", response.body()?.refreshToken)
                tokenSharedPreferencesEditor.apply()
            }
            else{
                Log.d("MyInformationEditmodeFragment_TAG_refreshToken", response.body().toString())
            }
        }
        refreshTokenThread.start()
        refreshTokenThread.join()
    // 비동기 통신 주석
//        RetrofitClient.instance.refreshToken(accessToken = "Bearer $accessToken",JsonParser.parseString(jsonObject.toString()))
//            .enqueue(object : Callback<RefreshTokenResponse>{
//                override fun onResponse(
//                    call: Call<RefreshTokenResponse>,
//                    response: Response<RefreshTokenResponse>
//                ) {
//                    if(response.isSuccessful){
//
//                    }
//                    else{
//                    }
//                }
//                override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
//                    Log.d("MyInformationEditmodeFragment_TAG", t.toString())
//                }
//            })
    }

    private fun retryLogout(accessToken: String, studentId:String,intent:Intent){
        RetrofitClient.instance.logout(accessToken = "Bearer ${accessToken}", studentId = studentId)
            .enqueue(object : Callback<LogoutResponse>{
                override fun onResponse(
                    call: Call<LogoutResponse>,
                    response: Response<LogoutResponse>
                ) {
                    Toast.makeText(requireContext(),"로그아웃이 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    requireActivity().finish()
                }
                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    //TODO("Not yet implemented")
                }
            })
    }
    private fun retryUpdateUserInfo(accessToken:String, jsonObject: JSONObject,myInformationFragment: MyInformationFragment){
        val updateUserInfoThread = Thread {
            val response = RetrofitClient.instance.updateUserInfo(accessToken = "Bearer $accessToken"
                ,JsonParser.parseString(jsonObject.toString())).execute()
            Log.d(TAG, response.toString())
            if(response.isSuccessful){
                val fragmentManager = parentFragmentManager.beginTransaction()
                fragmentManager.replace(R.id.fcv_all_fragment,myInformationFragment).commit()
            }
            else{
                Log.d("FaqFragment_TAG_retryUpdateUserInfo",response.toString())
            }
        }
        updateUserInfoThread.start()
        updateUserInfoThread.join()

//비동기 통신 주석
//        val response = RetrofitClient.instance.updateUserInfo(accessToken = "Bearer $accessToken",
//            JsonParser.parseString(jsonObject.toString())).execute()
//        if(response.isSuccessful){
//            val fragmentManager = parentFragmentManager.beginTransaction()
//            fragmentManager.replace(R.id.fcv_all_fragment,myInformationFragment).commit()
//        }
//        else{
//            Log.d("FaqFragment_TAG_retryUpdateUserInfo",response.toString())
//
//        }

//비동기 통신 주석
//        RetrofitClient.instance.updateUserInfo(accessToken = "Bearer $accessToken",
//            JsonParser.parseString(jsonObject.toString())).enqueue(object :Callback <UpdateUserInfoResponse>{
//            override fun onResponse(
//                call: Call <UpdateUserInfoResponse>,
//                response: Response<UpdateUserInfoResponse>
//            ) {
//                Log.d("FaqFragment_TAG_retryUpdateUserInfo", response.toString())
//                if(response.isSuccessful){
//
//                }
//                else{
//                }
//
//
//            }
//            override fun onFailure(call: Call<UpdateUserInfoResponse>, t: Throwable) {
//                Log.d("FaqFragment_TAG_retryUpdateUserInfo", t.toString())
//            }
//        })
    }


}