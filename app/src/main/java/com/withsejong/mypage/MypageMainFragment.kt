package com.withsejong.mypage

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.withsejong.R
import com.withsejong.databinding.FragmentMypageMainBinding
import com.withsejong.mypage.interestingList.InterestingListFragment

class MypageMainFragment : Fragment() {
    private lateinit var binding:FragmentMypageMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentMypageMainBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //밑줄을 위한코드
        binding.tvName.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        val interestingListFragment = InterestingListFragment()
        //val intentInterstingList = Intent(requireContext(),interestingListFragment::class.java)


        binding.cloInterestingList.setOnClickListener{
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fcv_all_fragment,interestingListFragment)
                .commit()

        }

    }



}