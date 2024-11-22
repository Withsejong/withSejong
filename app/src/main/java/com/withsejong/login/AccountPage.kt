package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.withsejong.databinding.ActivityAccountPageBinding
import com.withsejong.start.LoginChoicePage


class AccountPage : AppCompatActivity() {
    lateinit var binding: ActivityAccountPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //비번 잊으셨나요 페이지에서 온 경우 다시 로그인페이지로
        val loginPage = LoginPage()
        val intentLostPassword = Intent(this@AccountPage,loginPage::class.java)

        //회원가입 페이지에서 온 경우 login chice 페이지로 이동
        val loginChoicePage = LoginChoicePage()
        val intentSignup = Intent(this@AccountPage, loginChoicePage::class.java)


        val isSignup = intent.getStringExtra("isSignup").toString()
        binding.btnAuth.setOnClickListener {//세종인 인증 버튼 클릭시
            val intent = Intent(this@AccountPage, AccountInPage::class.java)
            intent.putExtra("isSignup",isSignup)
            startActivity(intent)
            finish()
        }

        if(isSignup=="1"){ //회원가입 페이지에서 온 경우 loginchoice page로 이동
            //휴대폰 뒤로가기를 누른 경우 이전 fragment로 돌아가는 행동 정의
            val backActionCallback = object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    startActivity(intentSignup)
                    finish()
                }
            }
            this@AccountPage.onBackPressedDispatcher.addCallback(this@AccountPage,backActionCallback)
        }
        else{//비번잊으셨나요 페이지에서 온 경우

            //휴대폰 뒤로가기를 누른 경우 이전 fragment로 돌아가는 행동 정의
            val backActionCallback = object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    startActivity(intentLostPassword)
                    finish()
                }
            }
            this@AccountPage.onBackPressedDispatcher.addCallback(this@AccountPage,backActionCallback)
        }



    }
}