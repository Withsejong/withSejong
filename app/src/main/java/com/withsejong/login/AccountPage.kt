package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.withsejong.R
import com.withsejong.databinding.ActivityAccountPageBinding
import com.withsejong.databinding.ActivityLoginChoicePageBinding


class AccountPage : AppCompatActivity() {
    lateinit var binding: ActivityAccountPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAuth.setOnClickListener {//세종인 인증 버튼 클릭시
            val intent = Intent(this@AccountPage, AccountInPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}