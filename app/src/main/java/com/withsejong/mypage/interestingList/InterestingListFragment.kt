package com.withsejong.mypage.interestingList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.withsejong.R
import com.withsejong.databinding.FragmentInterestingListBinding
import com.withsejong.mypage.MypageMainFragment

class InterestingListFragment : Fragment() {
    private lateinit var binding:FragmentInterestingListBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentInterestingListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myPageMainFragment = MypageMainFragment()
        binding.ibtnBack.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fcv_all_fragment,myPageMainFragment).commit()
        }
    }

    //TODO 나중에 관심목록에 들어가는 것들을 리사이클러뷰로 구현할 것
    //TODO 뒤로가기 구현 - 휴대폰 뒤로가기 말하는거임


}