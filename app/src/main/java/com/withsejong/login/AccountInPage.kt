package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.databinding.ActivityAccountInPageBinding
import com.withsejong.databinding.ActivityAccountPageBinding
import com.withsejong.databinding.ActivityLoginChoicePageBinding
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.SejongAuthResponse
import com.withsejong.start.LoginStartPage
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class AccountInPage : AppCompatActivity() {
    lateinit var binding: ActivityAccountInPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, MakePasswordPage::class.java)
        binding.btnNext.setOnClickListener {
            if (binding.etStudentIdInput.text.isNullOrEmpty() || binding.etStudentPasswordInput.text.isNullOrEmpty()) {
                Toast.makeText(this, "학번 또는 비밀번호를 입력 해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                //학번 중복체크 후 학정시 id, 비번이 맞는지 체크
                checkisDuplicatedID()
            }
        }
    }
    private fun checkisDuplicatedID(){
        //학번 중복 체크
        var isDuplicatedId: Int = 1
        Log.d("AccountInPage_TAG", binding.etStudentIdInput.text.toString())
        RetrofitClient.instance.IsDuplicatedID(binding.etStudentIdInput.text.toString())
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

                    Log.d("AccountInPage_TAG", response.toString())
                    if (response.isSuccessful) {
                        isDuplicatedId = 0
                        checkvalidID()

                    }
                    else{
                        binding.tvDuplicatedIdErrorIndicator.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {

                }

            })
    }

    private fun checkvalidID(){

            val jsonObject = JSONObject()
            jsonObject.put("id", binding.etStudentIdInput.text.toString())
            jsonObject.put("pw", binding.etStudentPasswordInput.text.toString())
            Log.d("AccountInPage_json", jsonObject.toString())





            RetrofitClient.onlySejongAuth.checkSejong(JsonParser.parseString(jsonObject.toString()))
                .enqueue(object : Callback<SejongAuthResponse> {
                    override fun onResponse(
                        call: Call<SejongAuthResponse>,
                        response: Response<SejongAuthResponse>
                    ) {

                        if (response.isSuccessful) {
                            Log.d("AccountInPage", response.body().toString())
                            if (response.body()?.result?.is_auth.toBoolean()) {
                                intent.putExtra(
                                    "id",
                                    binding.etStudentIdInput.text.toString()
                                )
                                intent.putExtra(
                                    "name",
                                    response.body()?.result?.body?.name.toString()
                                )
                                intent.putExtra(
                                    "major",
                                    response.body()?.result?.body?.major.toString()
                                )
                                Log.d(
                                    "AccountInPage_TAG",
                                    response.body()?.result?.body?.name.toString()
                                )
                                startActivity(intent)
                                finish()
                            } else {//학사정보시스템의 id, 비번이 일치하지 않은 경우
                                Toast.makeText(
                                    this@AccountInPage,
                                    "아이디 또는 비밀번호가 일치하지 않습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Log.d("AccountInpage", "실패")
                        }
                    }

                    override fun onFailure(call: Call<SejongAuthResponse>, t: Throwable) {
                        Log.d("AccountInPage", "onFailure")
                    }
                })

    }
}