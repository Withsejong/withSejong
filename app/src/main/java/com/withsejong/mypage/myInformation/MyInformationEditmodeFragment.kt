package com.withsejong.mypage.myInformation

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.withsejong.R
import com.withsejong.databinding.FragmentMyInformationEditmodeBinding


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
        val saveNickname = userInfoSharedPreferences.getString("nickname","error")
        val saveMajor = userInfoSharedPreferences.getString("major","error")
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

            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fcv_all_fragment,myInformationFragment).commit()
        }

    }


}