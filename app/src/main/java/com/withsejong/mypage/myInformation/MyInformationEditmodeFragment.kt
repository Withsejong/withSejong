package com.withsejong.mypage.myInformation

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
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.databinding.FragmentMyInformationEditmodeBinding
import com.withsejong.retrofit.LogoutResponse
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.UpdateUserInfoResponse
import com.withsejong.start.LoginChoicePage
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyInformationEditmodeFragment : Fragment() {

    private lateinit var binding : FragmentMyInformationEditmodeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyInformationEditmodeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfoSharedPreferences = requireActivity().getSharedPreferences("userInfo",MODE_PRIVATE)
        val tokenSharedPreferences = requireContext().getSharedPreferences("token", MODE_PRIVATE)
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
            RetrofitClient.instance.updateUserInfo(
                accessToken = "Bearer ${tokenSharedPreferences.getString("accessToken","error")}"
                ,JsonParser.parseString(jsonObject.toString()))
                .enqueue(object :  Callback<UpdateUserInfoResponse> {
                    override fun onResponse(
                        call: Call<UpdateUserInfoResponse>,
                        response: Response<UpdateUserInfoResponse>
                    ) {
                        if(response.isSuccessful){
                            val fragmentManager = parentFragmentManager.beginTransaction()
                            fragmentManager.replace(R.id.fcv_all_fragment,myInformationFragment).commit()
                        }
                        else{
                            Log.d("MyInformationEditmodeFragment_TAG",response.toString())
                        }
                    }

                    override fun onFailure(call: Call<UpdateUserInfoResponse>, t: Throwable) {
                        Log.d("MyInformationEditmodeFragment_TAG", t.toString())
                    }

                })


        }
        val intentLogout = Intent(requireActivity(), LoginChoicePage::class.java)
        binding.btnLogout.setOnClickListener {
            val saveId = userInfoSharedPreferences.getString("studentId","error").toString()
            val accessToken = tokenSharedPreferences.getString("accessToken","error").toString()
            RetrofitClient.instance.logout(accessToken = "Bearer $accessToken",studentId = saveId).enqueue(object : Callback<LogoutResponse>{
                override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                    Log.d("MyInformationEditmodeFragment_TAG",response.toString())
                    if(response.isSuccessful){
                        //Snackbar.make(requireView(),"로그아웃이 정상적으로 처리되었습니다.",Snackbar.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(),"로그아웃이 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(intentLogout)
                    }
                }
                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    Log.d("MyInformationEditmodeFragment_TAG",t.toString())
                }

            })
        }

    }


}