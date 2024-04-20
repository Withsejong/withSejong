package com.withsejong.start

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.withsejong.databinding.ActivityLoginChoicePageBinding
import com.withsejong.login.LoginPage
import com.withsejong.login.AccountPage


class LoginChoicePage : AppCompatActivity() {
    lateinit var binding: ActivityLoginChoicePageBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginChoicePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ){
            //TODO 여기에 뭘 써야할까
        }
        Log.d("LoginChoicePage_TAG", Build.VERSION.SDK_INT.toString())
        //유저가 알림 동의를 했는지 체킹
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                !=PackageManager.PERMISSION_GRANTED){
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        Log.d("LoginChoicePage_TAG",ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS).toString())




        val intentLogin = Intent(this@LoginChoicePage, LoginPage::class.java)
        val intentSignup = Intent(this@LoginChoicePage, AccountPage::class.java)

        binding.btnLogin.setOnClickListener {
            startActivity(intentLogin)
            finish()

        }
        binding.btnSignup.setOnClickListener {
            intentSignup.putExtra("isSignup", "1")
            startActivity(intentSignup)
            finish()

        }

    }
}