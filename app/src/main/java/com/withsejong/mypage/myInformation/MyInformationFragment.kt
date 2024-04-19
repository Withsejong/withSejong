package com.withsejong.mypage.myInformation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.withsejong.R
import com.withsejong.databinding.FragmentMyInformationBinding
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.deleteAccountResponse
import com.withsejong.start.LoginChoicePage
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyInformationFragment : Fragment() {

    private lateinit var binding : FragmentMyInformationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyInformationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userInfoSharedPreferences = requireContext().getSharedPreferences("userInfo",Context.MODE_PRIVATE)
        val tokenSharedPreferences = requireContext().getSharedPreferences("token",Context.MODE_PRIVATE)
        binding.tvNicknameIndicator.text = "${userInfoSharedPreferences.getString("nickname","Error")}님"

        //회원탈퇴
        val intent = Intent(requireContext(),LoginChoicePage::class.java)

        binding.btnDeleteId.setOnClickListener {

            val accessToken = tokenSharedPreferences.getString("accessToken",null)
            val loadid = userInfoSharedPreferences.getString("studentId",null)

            RetrofitClient.instance.deleteAccount(accessToken.toString(),loadid.toString()).enqueue(object : Callback<deleteAccountResponse>{

                override fun onResponse(
                    call: Call<deleteAccountResponse>,
                    response: Response<deleteAccountResponse>
                ) {
                    Log.d("MyInformationFragment_TAG", response.toString())

                    if(response.isSuccessful){
                        startActivity(intent)

                    }
                }

                override fun onFailure(call: Call<deleteAccountResponse>, t: Throwable) {
                    Log.d("MyInformationFragment_TAG", t.toString())
                }

            })
        }

    }


}