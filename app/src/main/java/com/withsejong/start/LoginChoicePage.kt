package com.withsejong.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.withsejong.databinding.ActivityLoginChoicePageBinding
import com.withsejong.login.LoginPage
import com.withsejong.login.AccountPage


class LoginChoicePage : AppCompatActivity() {
    lateinit var binding: ActivityLoginChoicePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginChoicePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentLogin = Intent(this@LoginChoicePage, LoginPage::class.java)
        val intentSignup = Intent(this@LoginChoicePage, AccountPage::class.java)

        binding.btnLogin.setOnClickListener {
            startActivity(intentLogin)
            finish()

        }
        binding.btnSignup.setOnClickListener {
            startActivity(intentSignup)
            finish()

        }

    }




    override fun onStart() {
        super.onStart()





    }
}