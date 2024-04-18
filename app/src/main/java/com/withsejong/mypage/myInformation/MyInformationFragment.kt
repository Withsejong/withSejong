package com.withsejong.mypage.myInformation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.withsejong.R
import com.withsejong.databinding.FragmentMyInformationBinding


class MyInformationFragment : Fragment() {

    private lateinit var binding : FragmentMyInformationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyInformationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


}