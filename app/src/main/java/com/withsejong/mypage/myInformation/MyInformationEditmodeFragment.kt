package com.withsejong.mypage.myInformation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.withsejong.R
import com.withsejong.databinding.FragmentMyInformationBinding
import com.withsejong.databinding.FragmentMyInformationEditmodeBinding


class MyInformationEditmodeFragment : Fragment() {

    private lateinit var binding : FragmentMyInformationEditmodeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyInformationEditmodeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


}