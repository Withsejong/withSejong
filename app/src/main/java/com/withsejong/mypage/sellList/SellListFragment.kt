package com.withsejong.mypage.sellList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.withsejong.R
import com.withsejong.databinding.FragmentSellListBinding


class SellListFragment : Fragment() {
    private lateinit var binding : FragmentSellListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentSellListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


}