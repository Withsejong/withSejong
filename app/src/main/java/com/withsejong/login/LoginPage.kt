package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import com.withsejong.MainActivity
import com.withsejong.R
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
        val intentLost = Intent(this@LoginPage, AccountPage::class.java)
        val intentNext = Intent(this@LoginPage, MainActivity::class.java)
        binding.btnLostPassword.setOnClickListener {
            startActivity(intentLost)
            finish()
        }
        //입력 감지

        var idInputCheck:Int = 0
        var pwInputCheck:Int = 0
        binding.etStudentIdInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.etStudentIdInput.text.length>0){
                    idInputCheck=1
                    if(idInputCheck+pwInputCheck==2){
                        binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_input)
                    }
                }
                else{
                    idInputCheck=0
                    binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_noinput)
                }
                Log.d("LoginPage_TAG", (idInputCheck+pwInputCheck).toString())

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.etStudentPasswordInput.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.etStudentPasswordInput.text.length>0){
                    pwInputCheck=1
                    if(idInputCheck+pwInputCheck==2){
                        binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_input)
                    }
                }
                else{
                    pwInputCheck=0
                    binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_noinput)
                }
                Log.d("LoginPage_TAG", (idInputCheck+pwInputCheck).toString())

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })



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
                            val tokenSharedPreferences = getSharedPreferences("token",
                                MODE_PRIVATE)
                            val userInfoSharedPreferences = getSharedPreferences("userInfo",
                                MODE_PRIVATE)
                            val tokenEditor = tokenSharedPreferences.edit()
                            val userInfoEditor = userInfoSharedPreferences.edit()

                            //토큰들 쉐프에 저장
                            tokenEditor.putString("grantType", response.body()?.authToken?.grantType.toString())
                            tokenEditor.putString("accessToken", response.body()?.authToken?.accessToken.toString())
                            tokenEditor.putString("refreshToken", response.body()?.authToken?.refreshToken.toString())
                            tokenEditor.apply()

                            //학생정보들 쉐프에 저장
                            userInfoEditor.putString("studentId",response.body()?.studentId)
                            userInfoEditor.putString("nickname", response.body()?.nickname)
                            userInfoEditor.putString("major",response.body()?.major)
                            userInfoEditor.apply()

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