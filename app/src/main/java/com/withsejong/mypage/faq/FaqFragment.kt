package com.withsejong.mypage.faq

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.databinding.FragmentFaqBinding
import com.withsejong.mypage.MypageMainFragment
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.LoadFaqResponse
import com.withsejong.retrofit.RefreshTokenResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqFragment : Fragment() {

    private lateinit var binding:FragmentFaqBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaqBinding.inflate(layoutInflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userInfoSharedPreferences = requireContext().getSharedPreferences("userInfo",
            Context.MODE_PRIVATE
        )
        val tokenSharedPreferences = requireContext().getSharedPreferences("token",Context.MODE_PRIVATE)
        val saveID = userInfoSharedPreferences.getString("studentId", "Error")
        val saveAccessToken = tokenSharedPreferences.getString("accessToken","Error")
        val saveRefreshToken = tokenSharedPreferences.getString("refreshToken", "Error")




        //상단 뒤로가기 버튼 동작 정의
        val mypageMainFragment = MypageMainFragment()



        binding.ibtnBack.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fcv_all_fragment,mypageMainFragment).commit()
        }
        //휴대폰 뒤로가기를 누른 경우 이전 fragment로 돌아가는 행동 정의
        val backActionCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragmentManager = parentFragmentManager.beginTransaction()
                fragmentManager.replace(R.id.fcv_all_fragment,mypageMainFragment).commit()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),backActionCallback)


        RetrofitClient.instance.loadFaq(accessToken = "Bearer ${tokenSharedPreferences.getString("accessToken","error")}").enqueue(object : Callback<ArrayList<LoadFaqResponse>>{
            override fun onResponse(
                call: Call<ArrayList<LoadFaqResponse>>,
                response: Response<ArrayList<LoadFaqResponse>>
            ) {
                if(response.code().toString()=="403"){
                    //TODO 토큰 리프레시 하는 api 연결
                    val jsonObject = JSONObject()
                    jsonObject.put("studentId", saveID)
                    jsonObject.put("accessToken", saveAccessToken)
                    jsonObject.put("refreshToken", saveRefreshToken)

                    RetrofitClient.instance.refreshToken(accessToken = "Bearer $saveAccessToken",JsonParser.parseString(jsonObject.toString()))
                        .enqueue(object :Callback<RefreshTokenResponse> {
                        override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
                            if(response.isSuccessful){
                                Log.d("FaqFragment_TAG,갱신 전", saveAccessToken.toString())
                                val accessToken = response.body()?.accessToken
                                val refreshToken = response.body()?.refreshToken
                                val tokenSharedPreferencesEditor = requireContext().getSharedPreferences("token",Context.MODE_PRIVATE).edit()
                                tokenSharedPreferencesEditor.putString("accessToken",accessToken)
                                tokenSharedPreferencesEditor.putString("refreshToken", refreshToken)
                                tokenSharedPreferencesEditor.apply()
                                Log.d("FaqFragment_TAG,갱신 후", accessToken.toString())

                                //TODO 공지사항 다시 받는 코드 추가
                                reloadFaq(accessToken.toString())
                            }
                        }

                        override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                            Log.d("FaqFragment_TAG", t.toString())
                        }

                    })
                }

                else if(response.isSuccessful){//FAQ를 잘 받아 온 경우
                    Log.d("FaqFragment_TAG", response.toString())
                    val responseList : ArrayList<LoadFaqResponse>? = response.body()

                    binding.rcvNoticyList.adapter = FaqAdapter(responseList)
                    binding.rcvNoticyList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

                }
                else{
                    Log.d("FaqFragment_TAG", response.toString())

                }
            }

            override fun onFailure(call: Call<ArrayList<LoadFaqResponse>>, t: Throwable) {
                Log.d("FaqFragment_TAG", t.toString())
            }

        })


    }



    private fun reloadFaq(accessToken:String){
        RetrofitClient.instance.loadFaq(accessToken = "Bearer $accessToken").enqueue(object :Callback <ArrayList<LoadFaqResponse>>{
            override fun onResponse(
                call: Call<ArrayList<LoadFaqResponse>>,
                response: Response<ArrayList<LoadFaqResponse>>
            ) {
                Log.d("FaqFragment_TAG_reloadFaq", response.toString())
                val responseList : ArrayList<LoadFaqResponse>? = response.body()
                binding.rcvNoticyList.adapter = FaqAdapter(responseList)
                binding.rcvNoticyList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            }
            override fun onFailure(call: Call<ArrayList<LoadFaqResponse>>, t: Throwable) {
                Log.d("FaqFragment_TAG", t.toString())
            }
        })
    }

}