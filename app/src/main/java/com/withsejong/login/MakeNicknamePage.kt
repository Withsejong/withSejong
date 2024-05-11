package com.withsejong.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.databinding.ActivityMakeNicknamePageBinding
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



        binding.etNicknameInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.etNicknameInput.text.length>0){
                    binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_input)

                }
                else{
                    binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_noinput)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.btnNext.setOnClickListener {
            //TODO 닉네임이 중복되는 경우 에러띄워주는 tv visible
            //TODO 조건문으로 분기하여 중복되면 textview visible, 중복되지 않으면 화면전환
            //binding.tvDuplicatedNicknameErrorIndicator

            //닉네임 중복검사 후 계정 생성

            isDuplicatedNickname(saveName,saveId,savePassword,saveMajor,intent)



        }
    }



private fun isDuplicatedNickname(saveName:String?, saveId:String?, savePassword:String?, saveMajor:String?,intent: Intent){
    RetrofitClient.instance.isDuplicatedNickname(binding.etNicknameInput.text.toString()).enqueue(object : Callback<Boolean>{
        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
            if(response.isSuccessful){
                val jsonObject = JSONObject()
                jsonObject.put("name",saveName)
                jsonObject.put("studentId",saveId)
                jsonObject.put("password",savePassword)
                jsonObject.put("major",saveMajor)
                jsonObject.put("nickname",binding.etNicknameInput.text.toString())
                createAccount(saveId,savePassword,jsonObject,intent)
            }
            else{
                binding.tvDuplicatedNicknameErrorIndicator.visibility = View.VISIBLE
            }
        }
        override fun onFailure(call: Call<Boolean>, t: Throwable) {

        }

    })
}
    private fun createAccount(saveId: String?, savePassword: String?,jsonObject:JSONObject,intent: Intent){
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
                Log.d("MakeNicknamePage_TAG",response.body().toString())
            }
            override fun onFailure(call: Call<UserSignupResponse>, t: Throwable) {
                Log.d("MakeNicknamePage_TAG", call.toString())
                Log.d("MakeNicknamePage_TAG",t.toString())
                Log.d("MakeNicknamePage","완전실패")
            }
        })
    }
}