package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.withsejong.R
import com.withsejong.databinding.ActivityAccountInPageBinding
import com.withsejong.databinding.ActivityAccountPageBinding
import com.withsejong.databinding.ActivityLoginChoicePageBinding
import com.withsejong.start.LoginStartPage


class AccountInPage : AppCompatActivity() {
    lateinit var binding: ActivityAccountInPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountInPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, LoginStartPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}