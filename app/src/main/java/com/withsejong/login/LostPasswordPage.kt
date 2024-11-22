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
            //다음으로 못넘어 가는 조건 : 2 비번이 다를 때, 둘중 하나라도 입력 안했을 때,
            // (optional) 비번 규칙에 맞지 않을 때
            //TODO 비번 칸 둘다 입력되었을 경우 버튼 색 변하게4

            if(binding.etNewPasswordInput.text.toString()!=binding.etNewPasswordOkInput.text.toString()){
                //우선 토스트 메시지로 표시
                binding.tvNotAccordPasswordErrorIndicator.visibility = View.VISIBLE
            }
            else if(binding.etNewPasswordInput.text.length==0 || binding.etNewPasswordOkInput.text.length==0){
                Toast.makeText(this,"비어있는 칸이 있습니다!", Toast.LENGTH_SHORT).show()
            }
            else{
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
                            Toast.makeText(this@LostPasswordPage,"비밀번호 변경이 완료되었습니다.",Toast.LENGTH_SHORT).show()
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



        //입력 감지

        var idInputCheck:Int = 0
        var pwInputCheck:Int = 0
        binding.etNewPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.etNewPasswordInput.text.length>0){
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

        binding.etNewPasswordOkInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.etNewPasswordOkInput.text.length>0){
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
    }
}