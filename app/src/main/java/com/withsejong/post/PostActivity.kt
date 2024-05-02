package com.withsejong.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.withsejong.R
import com.withsejong.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}