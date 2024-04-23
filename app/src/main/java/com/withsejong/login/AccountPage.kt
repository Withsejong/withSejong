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


        val isSignup = intent.getStringExtra("isSignup").toString()
        binding.btnAuth.setOnClickListener {//세종인 인증 버튼 클릭시
            val intent = Intent(this@AccountPage, AccountInPageSync::class.java)
            intent.putExtra("isSignup",isSignup)
            startActivity(intent)
            finish()
        }
    }
}