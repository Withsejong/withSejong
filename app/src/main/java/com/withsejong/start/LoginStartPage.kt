package com.withsejong.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.withsejong.R
import com.withsejong.databinding.ActivityLoginStartPageBinding
import com.withsejong.login.LoginPage

class LoginStartPage : AppCompatActivity() {

    val TAG = "ActivityLoginChoicePage"

    lateinit var binding:ActivityLoginStartPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginStartPageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val saveId = intent.getStringExtra("id")
        val savePassword = intent.getStringExtra("password")
        val saveNickname = intent.getStringExtra("nickname")

        binding.tvWelcomeNameText.text=
            """환영합니다 ${saveNickname}님
            세종인끼리와 간편한 책거래를 시작해 보아요 :)""".trimMargin()

        Log.d(TAG,"onCreate함수")

        binding.btnStart.setOnClickListener {
            val intent = Intent(this@LoginStartPage,LoginPage::class.java)
            Log.d(TAG, "버튼 클릭됨")
            startActivity(intent)
            finish()
        }

    }
}