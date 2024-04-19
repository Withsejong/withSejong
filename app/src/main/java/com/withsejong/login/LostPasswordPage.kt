package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser

import com.withsejong.databinding.ActivityLostPasswordPageBinding
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.changeForgotPassword
import com.withsejong.start.LoginStartPage
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
        val intent = Intent(this@LostPasswordPage, LoginPage::class.java)


        binding.btnNext.setOnClickListener {

            if(binding.etNewPasswordOkInput.text.length>0) {
                val jsonObject=JSONObject()
                jsonObject.put("studentId",saveId)
                jsonObject.put("password",binding.etNewPasswordOkInput.text)
                RetrofitClient.instance.changeForgotPassword(JsonParser.parseString(jsonObject.toString())).
                enqueue(object : Callback<changeForgotPassword>{
                    override fun onResponse(
                        call: Call<changeForgotPassword>,
                        response: Response<changeForgotPassword>
                    ) {
                        if(response.isSuccessful){
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<changeForgotPassword>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })


            }



        }
    }
}