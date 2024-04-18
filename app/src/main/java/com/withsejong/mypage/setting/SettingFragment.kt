package com.withsejong.mypage.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.withsejong.R
import com.withsejong.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding:FragmentSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


}