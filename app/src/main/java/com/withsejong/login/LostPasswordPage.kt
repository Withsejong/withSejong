package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser

import com.withsejong.databinding.ActivityLostPasswordPageBinding
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.ChangeForgotPassword
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LostPasswordPage : AppCompatActivity() {
    private lateinit var binding: ActivityLostPasswordPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLostPasswordPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val saveId = intent.getStringExtra("studentId")
        Log.d("LostPasswordPage_TAG_saveId", saveId.toString())
        val saveNickname = intent.getStringExtra("nickname")

        binding.tvAccountOk.text = """${saveNickname}님, 세종인 인증이 완료되었습니다.
            |새롭게 사용할 비밀번호를 입력해 주세요 :D""".trimMargin()



        val intent = Intent(this@LostPasswordPage, LoginPage::class.java)


        binding.btnNext.setOnClickListener {

            //TODO 비번 같은지 확인하는 로직 필요
            if(binding.etNewPasswordOkInput.text.length>0) {
                val jsonObject=JSONObject()
                jsonObject.put("studentId",saveId)
                jsonObject.put("password",binding.etNewPasswordOkInput.text)
                Log.d("LostPasswordPage_TAG", jsonObject.toString())
                RetrofitClient.instance.changeForgotPassword(JsonParser.parseString(jsonObject.toString())).
                enqueue(object : Callback<ChangeForgotPassword>{
                    override fun onResponse(
                        call: Call<ChangeForgotPassword>,
                        response: Response<ChangeForgotPassword>
                    ) {
                        Log.d("LostPasswordPage_TAG",response.toString())
                        if(response.isSuccessful){
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<ChangeForgotPassword>, t: Throwable) {
                        Log.d("LostPasswordPage_TAG", t.toString())
                    }
                })
            }



        }
    }
}