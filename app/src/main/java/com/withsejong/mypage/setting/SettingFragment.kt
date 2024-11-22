package com.withsejong.mypage.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.withsejong.R
import com.withsejong.databinding.FragmentSettingBinding
import com.withsejong.mypage.MypageMainFragment

class SettingFragment : Fragment() {
    private lateinit var binding:FragmentSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }

}