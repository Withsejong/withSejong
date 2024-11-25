package com.withsejong.mypage.myInformation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.withsejong.R
import com.withsejong.databinding.FragmentMyInformationBinding
import com.withsejong.mypage.MypageMainFragment
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.LogoutResponse
import com.withsejong.start.LoginChoicePage
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
        val saveNickname = userInfoSharedPreferences.getString("nickname","Error")
        val saveMajor = userInfoSharedPreferences.getString("major","Error")
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


        //닉네임과 소속 학과 sharedpreference에 저장 된 것으로 설정
        binding.tvNicknameIndicator.text = "${saveNickname}님"
        binding.etNicknameIndicator.setText("$saveNickname")
        binding.etMajorIndicator.setText("${saveMajor}")

        //edittext를 editable하지 않게 변경
        binding.etNicknameIndicator.isEnabled = false
        binding.etMajorIndicator.isEnabled = false
        //회원탈퇴
        val intent = Intent(requireContext(),LoginChoicePage::class.java)

        //editmode fragment 정의
        val myInformationEditmodeFragment = MyInformationEditmodeFragment()
        binding.ibtnEditmode.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fcv_all_fragment,myInformationEditmodeFragment).commit()
        }
        //회원탈퇴 버튼 기능
            val accountDeleteDialogFragment=AccountDeleteDialogFragment()
        binding.btnDeleteId.setOnClickListener {

            val accessToken = tokenSharedPreferences.getString("accessToken",null)
            val saveId = userInfoSharedPreferences.getString("studentId",null)
//통신부분 일시 주석처리
//            RetrofitClient.instance.deleteAccount(accessToken = "Bearer $accessToken",saveId.toString()).enqueue(object : Callback<DeleteAccountResponse>{
//
//                override fun onResponse(
//                    call: Call<DeleteAccountResponse>,
//                    response: Response<DeleteAccountResponse>
//                ) {
//                    Log.d("MyInformationFragment_TAG", response.toString())
//
//                    if(response.isSuccessful){
//                        startActivity(intent)
//
//
//                    }
//                }
//                override fun onFailure(call: Call<DeleteAccountResponse>, t: Throwable) {
//                    Log.d("MyInformationFragment_TAG", t.toString())
//                }
//            })
            accountDeleteDialogFragment.show(parentFragmentManager,"")


        }
            val intentLogout = Intent(requireActivity(),LoginChoicePage::class.java)
        binding.btnLogout.setOnClickListener {



            val saveId = userInfoSharedPreferences.getString("studentId","error").toString()
            val accessToken = tokenSharedPreferences.getString("accessToken","error").toString()
            RetrofitClient.instance.logout(accessToken = "Bearer $accessToken",studentId = saveId).enqueue(object : Callback<LogoutResponse>{
                override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                    Log.d("MyInformationFragment_TAG",response.toString())
                    if(response.isSuccessful){
                        //Snackbar.make(requireView(),"로그아웃이 정상적으로 처리되었습니다.",Snackbar.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(),"로그아웃이 정상적으로 처리되었습니다.",Toast.LENGTH_SHORT).show()
                        startActivity(intentLogout)
                    }
                }
                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    Log.d("MyInformationFragment_TAG",t.toString())
                }

            })
        }
    }


}