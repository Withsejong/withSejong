package com.withsejong.mypage.setting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.withsejong.BuildConfig
import com.withsejong.R
import com.withsejong.databinding.FragmentSettingBinding
import com.withsejong.mypage.MypageMainFragment

class SettingFragment : Fragment() {
    private lateinit var binding:FragmentSettingBinding
    private val TAG = "SettingFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //상단 뒤로가기 버튼 동작 정의
        val mypageMainFragment = MypageMainFragment()

        binding.tvVersion.text = "앱 버전 : ${BuildConfig.APPVERSION}"

        //TODO 토큰 카피 코드는 반드시 지울 것

        binding.cloTokenCopy.setOnClickListener {
            val token = requireActivity().getSharedPreferences("token", MODE_PRIVATE).getString("fcmToken","error")
            Log.d(TAG,token.toString())
            // ContextCompat를 사용하여 ClipboardManager 가져오기
            val clipboard = ContextCompat.getSystemService(requireActivity(), ClipboardManager::class.java)

            if (clipboard != null) {
                val clip = ClipData.newPlainText("Copied Text", token)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireActivity(), "복사되었습니다!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "클립보드를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }


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