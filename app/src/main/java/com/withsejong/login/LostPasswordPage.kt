package com.withsejong.login

import android.content.Intent
import android.os.Bundle
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

        val saveId = intent.getStringExtra("id")
        binding.tvAccountOk.text = """${saveId}님, 세종인 인증이 완료되었습니다.
            |새롭게 사용할 비밀번호를 입력해 주세요 :D""".trimMargin()



        val intent = Intent(this@LostPasswordPage, LoginPage::class.java)


        binding.btnNext.setOnClickListener {

            if(binding.etNewPasswordOkInput.text.length>0) {
                val jsonObject=JSONObject()
                jsonObject.put("studentId",saveId)
                jsonObject.put("password",binding.etNewPasswordOkInput.text)
                RetrofitClient.instance.changeForgotPassword(JsonParser.parseString(jsonObject.toString())).
                enqueue(object : Callback<ChangeForgotPassword>{
                    override fun onResponse(
                        call: Call<ChangeForgotPassword>,
                        response: Response<ChangeForgotPassword>
                    ) {
                        if(response.isSuccessful){
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ChangeForgotPassword>, t: Throwable) {
                        //TODO("Not yet implemented")
                    }

                })


            }



        }
    }
}