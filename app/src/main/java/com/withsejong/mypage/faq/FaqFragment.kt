package com.withsejong.mypage.faq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.withsejong.R
import com.withsejong.databinding.FragmentFaqBinding

class FaqFragment : Fragment() {

    private lateinit var binding:FragmentFaqBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaqBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

}