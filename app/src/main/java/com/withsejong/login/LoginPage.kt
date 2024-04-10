package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.withsejong.R
import com.withsejong.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {
    lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLostPassword.setOnClickListener {
            val intent = Intent(this@LoginPage, LostPasswordPage::class.java)
            startActivity(intent)
            finish()

        }
    }

    override fun onStart() {
        super.onStart()


    }



}