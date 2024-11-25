package com.withsejong.mypage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.MODE_PRIVATE
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.withsejong.BuildConfig
import com.withsejong.R
import com.withsejong.databinding.FragmentMypageMainBinding
import com.withsejong.mypage.buyList.BuyListFragment
import com.withsejong.mypage.faq.FaqFragment
import com.withsejong.mypage.interestingList.InterestingListFragment
import com.withsejong.mypage.myInformation.MyInformationFragment
import com.withsejong.mypage.sellList.SellListFragment
import com.withsejong.mypage.setting.SettingFragment

class MypageMainFragment : Fragment() {
    private lateinit var binding:FragmentMypageMainBinding
    private val TAG:String = "MypageMainFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentMypageMainBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfoSharedPreferences = requireActivity().getSharedPreferences("userInfo",
            MODE_PRIVATE
        )
        val loadedName = userInfoSharedPreferences.getString("nickname", "Error")

        binding.tvName.text = "${loadedName}님"

        //밑줄을 위한코드
        binding.tvName.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        val interestingListFragment = InterestingListFragment()
        val sellListFragment = SellListFragment()
        val buyListFragment = BuyListFragment()
        val myInformationFragment = MyInformationFragment()
        val settingFragment = SettingFragment()
        val faqFragment = FaqFragment()

        binding.tvVersion.text = "앱 버전 : ${BuildConfig.APPVERSION}"

        //TODO 토큰 카피 코드는 반드시 지울 것
        binding.tvTokencopy.setOnClickListener {
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





        //val intentInterstingList = Intent(requireContext(),interestingListFragment::class.java)


        binding.cloInterestingList.setOnClickListener{
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fcv_all_fragment,interestingListFragment)
                .commit()

        }

        binding.cloSellList.setOnClickListener{
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fcv_all_fragment,sellListFragment)
                .commit()
        }
        binding.cloBuyList.setOnClickListener {
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fcv_all_fragment,buyListFragment)
                .commit()
        }
        binding.cloMyInformation.setOnClickListener {
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fcv_all_fragment,myInformationFragment)
                .commit()
        }
        binding.cloSetting.setOnClickListener {
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fcv_all_fragment,settingFragment)
                .commit()
        }
        binding.cloFaq.setOnClickListener {
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fcv_all_fragment,faqFragment)
                .commit()
        }



    }



}