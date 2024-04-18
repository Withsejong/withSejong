package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import com.withsejong.MainActivity
import com.withsejong.databinding.ActivityLoginPageBinding
import com.withsejong.retrofit.LoginResponse
import com.withsejong.retrofit.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

        class LoginPage : AppCompatActivity() {
    lateinit var binding: ActivityLoginPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //TODO 아이디를 숫자만 입력받게끔 변경
        val intentLost = Intent(this@LoginPage, LostPasswordPage::class.java)
        val intentNext = Intent(this@LoginPage, MainActivity::class.java)
        binding.btnLostPassword.setOnClickListener {
            startActivity(intentLost)
            finish()
        }
        binding.btnNext.setOnClickListener {

            val jsonObject=JSONObject()
            jsonObject.put("studentId",binding.etStudentIdInput.text)
            jsonObject.put("password",binding.etStudentPasswordInput.text)

            RetrofitClient.instance.login(JsonParser.parseString(jsonObject.toString()))
                .enqueue(object :Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.isSuccessful){
                            val tokenSharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
                            val editor = tokenSharedPreferences.edit()
                            editor.putString("grantType", response.body()?.grantType.toString())
                            editor.putString("accessToken", response.body()?.accessToken.toString())
                            editor.putString("refreshToken", response.body()?.refreshToken.toString())
                            editor.apply()

                            Log.d("LoginPage_TAG", tokenSharedPreferences.getString("refreshToken","Error").toString())



                            startActivity(intentNext)
                            finish()
                        }
                        else{
                            Log.d("LoginPage_TAG",response.code().toString())
                            Toast.makeText(this@LoginPage,"아이디 혹은 비밀번호를 확인해주세요!",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                    }
                })
        }
    }
}