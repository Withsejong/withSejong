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

        Log.d(TAG,"onCreate함수")

        binding.btnStart.setOnClickListener {
            val intent = Intent(this@LoginStartPage,LoginPage::class.java)

            Log.d(TAG, "버튼 클릭됨")
            startActivity(intent)
            finish()

        }

    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart함수")








    }
}