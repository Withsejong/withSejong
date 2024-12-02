package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import com.withsejong.MainActivity
import com.withsejong.R
import com.withsejong.databinding.ActivityLoginPageBinding
import com.withsejong.retrofit.LoginResponse
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.start.LoginChoicePage
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
        val intentLost = Intent(this@LoginPage, AccountPage::class.java)
        val intentNext = Intent(this@LoginPage, MainActivity::class.java)
        binding.btnLostPassword.setOnClickListener {
            startActivity(intentLost)
            finish()
        }
        //뒤로가기 시 loginchice페이지로 이동
        val loginChoicePage = LoginChoicePage()

        //휴대폰 뒤로가기를 누른 경우 이전 fragment로 돌아가는 행동 정의
        val backActionCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@LoginPage, loginChoicePage::class.java)
                startActivity(intent)
                finish()
            }
        }
        this@LoginPage.onBackPressedDispatcher.addCallback(this@LoginPage, backActionCallback)
        //입력 감지

        var idInputCheck: Int = 0
        var pwInputCheck: Int = 0
        binding.etStudentIdInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.etStudentIdInput.text.length > 0) {
                    idInputCheck = 1
                    if (idInputCheck + pwInputCheck == 2) {
                        binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_input)
                    }
                } else {
                    idInputCheck = 0
                    binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_noinput)
                }
                Log.d("LoginPage_TAG", (idInputCheck + pwInputCheck).toString())

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.etStudentPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.etStudentPasswordInput.text.length > 0) {
                    pwInputCheck = 1
                    if (idInputCheck + pwInputCheck == 2) {
                        binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_input)
                    }
                } else {
                    pwInputCheck = 0
                    binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_noinput)
                }
                Log.d("LoginPage_TAG", (idInputCheck + pwInputCheck).toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.btnNext.setOnClickListener {
            Toast.makeText(this, "로그인 진행중 입니다. 잠시 기다려주세요.", Toast.LENGTH_SHORT).show()
            val tokenSharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
            val fcmToken = tokenSharedPreferences.getString("fcmToken", null)

            val jsonObject = JSONObject()
            jsonObject.put("studentId", binding.etStudentIdInput.text)
            jsonObject.put("password", binding.etStudentPasswordInput.text)
            jsonObject.put("fcmToken", fcmToken)
            RetrofitClient.instance.login(JsonParser.parseString(jsonObject.toString()))
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        Log.d("LoginPage_TAG", response.toString())

                        if (response.isSuccessful) {
                            val tokenSharedPreferences = getSharedPreferences(
                                "token",
                                MODE_PRIVATE
                            )
                            val userInfoSharedPreferences = getSharedPreferences(
                                "userInfo",
                                MODE_PRIVATE
                            )
                            val tokenEditor = tokenSharedPreferences.edit()
                            val userInfoEditor = userInfoSharedPreferences.edit()

                            //토큰들 쉐프에 저장
                            tokenEditor.putString(
                                "grantType",
                                response.body()?.authToken?.grantType.toString()
                            )
                            tokenEditor.putString(
                                "accessToken",
                                response.body()?.authToken?.accessToken.toString()
                            )
                            tokenEditor.putString(
                                "refreshToken",
                                response.body()?.authToken?.refreshToken.toString()
                            )
                            tokenEditor.apply()
                            Log.d("LoginPage_TAG", response.body().toString())
                            Log.d(
                                "LoginPage_TAG",
                                tokenSharedPreferences.getString("accessToken", "def").toString()
                            )
                            //학생정보들 쉐프에 저장
                            userInfoEditor.putString("studentId", response.body()?.studentId)
                            userInfoEditor.putString("nickname", response.body()?.nickname)
                            userInfoEditor.putString("major", response.body()?.major)
                            userInfoEditor.apply()

                            Log.d(
                                "LoginPage_TAG",
                                tokenSharedPreferences.getString("refreshToken", "Error").toString()
                            )
                            startActivity(intentNext)
                            finish()
                        } else {
                            Log.d("LoginPage_TAG", response.code().toString())
                            Toast.makeText(
                                this@LoginPage,
                                "아이디 혹은 비밀번호를 확인해주세요!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("LoginPage_TAG", t.toString())

                    }
                })
        }
    }
}