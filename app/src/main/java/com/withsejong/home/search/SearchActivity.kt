package com.withsejong.home.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.withsejong.R
import com.withsejong.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //X버튼 누를경우 검색어 다 지우는 코드
        binding.ibtnClear.setOnClickListener {
            binding.etSearch.setText("")
        }
    }
}