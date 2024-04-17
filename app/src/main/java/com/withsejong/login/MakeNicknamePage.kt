package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import com.withsejong.databinding.ActivityMakeNicknamePageBinding
import com.withsejong.databinding.ActivityMakePasswordPageBinding
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.UserSignupResponse
import com.withsejong.start.LoginStartPage
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeNicknamePage:AppCompatActivity() {

    private lateinit var binding:ActivityMakeNicknamePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMakeNicknamePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val saveId = intent.getStringExtra("id")
        val saveName = intent.getStringExtra("name")
        val savePassword = intent.getStringExtra("password")
        val saveMajor = intent.getStringExtra("major")
        val intent = Intent(this, LoginStartPage::class.java)


        val jsonObject = JSONObject()
        jsonObject.put("name",saveName)
        jsonObject.put("studentId",saveId)
        jsonObject.put("password",savePassword)
        jsonObject.put("major",saveMajor)


        binding.btnNext.setOnClickListener {
            //TODO 닉네임이 중복되는 경우 에러띄워주는 tv visible
            //TODO 조건문으로 분기하여 중복되면 textview visible, 중복되지 않으면 화면전환
            //binding.tvDuplicatedNicknameErrorIndicator
            Log.d("MakeNicknamePage_TAG", "btnNext 클릭감지")
            jsonObject.put("nickname",binding.etNicknameInput.text.toString())
            Log.d("MakeNicknamePage_TAG",jsonObject.toString())

            RetrofitClient.instance.signup(JsonParser.parseString(jsonObject.toString())).
            enqueue(object: Callback<UserSignupResponse> {
                override fun onResponse(
                    call: Call<UserSignupResponse>,
                    response: Response<UserSignupResponse>
                ) {
                    if(response.isSuccessful){
                        intent.putExtra("id",saveId)
                        intent.putExtra("password",savePassword)
                        intent.putExtra("nickname",binding.etNicknameInput.text.toString())
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@MakeNicknamePage,"계정생성완료",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@MakeNicknamePage,"계정생성실패",Toast.LENGTH_SHORT).show()
                    }
                    Log.d("MakeNicknamePage_TAG",response.code().toString())
                }

                override fun onFailure(call: Call<UserSignupResponse>, t: Throwable) {
                    Log.d("MakeNicknamePage_TAG", call.toString())
                    Log.d("MakeNicknamePage_TAG",t.toString())

                    Log.d("MakeNicknamePage","완전실패")
                }

            })



        }

    }
}