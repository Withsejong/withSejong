package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.withsejong.databinding.ActivityMakePasswordPageBinding
import com.withsejong.retrofit.RetrofitClient


class MakePasswordPage:AppCompatActivity() {

    private lateinit var binding:ActivityMakePasswordPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMakePasswordPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val saveId = intent.getStringExtra("id")
        Log.d("MakePasswordPage_TAG",saveId.toString())
        val saveName = intent.getStringExtra("name")
        val saveMajor = intent.getStringExtra("major")
        binding.tvId.text=saveId
        val intent = Intent(this,MakeNicknamePage::class.java)
        binding.btnNext.setOnClickListener {
            //TODO 비번 규칙에 맞으면 intent로 화면전환
            //TODO IF문으로 조건 분기할 것
            Log.d("MakePasswordPage_TAG",saveId.toString())
            Log.d("MakePasswordPage_TAG",saveName.toString())
            Log.d("MakePasswordPage_TAG",binding.tvPasswordText.text.toString())

            intent.putExtra("id",saveId)
            intent.putExtra("name",saveName)
            intent.putExtra("major",saveMajor)

            intent.putExtra("password",binding.etNewPasswordOkInput.text.toString())
            startActivity(intent)
            finish()
        }
    }
}