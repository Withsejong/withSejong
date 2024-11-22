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
import com.withsejong.databinding.ActivityAccountInPageBinding
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.SejongAuthResponse
import com.withsejong.retrofit.CheckStudentIdResponse
import okio.IOException
import org.json.JSONObject
import retrofit2.Response


class AccountInPage : AppCompatActivity() {
    lateinit var binding: ActivityAccountInPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val isSignup = intent.getStringExtra("isSignup").toString()
        val intent = Intent(this, MakePasswordPage::class.java)
        val intentChangeForgotPassword = Intent(this, LostPasswordPage::class.java)

        //입력 감지
        var idInputCheck:Int = 0
        var pwInputCheck:Int = 0
        binding.etStudentIdInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.etStudentIdInput.text.length>0){
                    idInputCheck=1
                    if(idInputCheck+pwInputCheck==2){
                        //binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_input)
                        binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_input)
                    }
                }
                else{
                    idInputCheck=0
                    binding.btnNext.setBackgroundResource(R.drawable.design_next_btn_noinput)
                }
                Log.d("AccountInPage", (idInputCheck+pwInputCheck).toString())

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.etStudentPasswordInput.addTextChangedListener(object: TextWatcher {
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
                Log.d("AccountInPage", (idInputCheck+pwInputCheck).toString())

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.btnNext.setOnClickListener {
            if (binding.etStudentIdInput.text.isNullOrEmpty() || binding.etStudentPasswordInput.text.isNullOrEmpty()) {
                Toast.makeText(this, "학번 또는 비밀번호를 입력 해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                //학번 중복체크 후 학정시 id, 비번이 맞는지 체크
                val sejongAuthReturnCode: Int
                if (isSignup == "1") {//회원가입 페이지에서 세종auth인증 페이지로 온 경우
                    val functionReturnCode =
                        checkisDuplicatedID(intent)//0은 실패 1은 활성계정 존재. 2는 재가입, 3은 계정이 존재하지 않음
                    when (functionReturnCode) {
                        3 -> {//세종오스 인증 회원가입
                            checkValidID(intent, 1)
                        }
                    }
                } else {//비번을 잊으셨나요? 페이지에서 세종auth인증 페이지로 온 경우
                    sejongAuthReturnCode = checkValidID(intentChangeForgotPassword, 0)
                }
            }
        }





    }

    private fun checkisDuplicatedID(intent: Intent): Int {
        //학번 중복 체크
        var checkValid: Int = -1
        var response: Response<CheckStudentIdResponse>
        Log.d("AccountInPage_TAG", binding.etStudentIdInput.text.toString())


        //동기 통신으로 변경 - 스레드풀 적용
        val checkDuplicatedIdThread = Thread {
            try {
                response =
                    RetrofitClient.instance.isDuplicatedID(binding.etStudentIdInput.text.toString())
                        .execute()
                if (response.isSuccessful) {
                    val result = response.body()
                    checkValid = -2
                    if (response.body()?.isSigned == true) {//이미 가입된 계정이 있는 경우
                        if (response.body()?.isDeleted == false) {//회원탈퇴 이력이 없는 경우 = db에 활성계정이 존재
                            runOnUiThread {
                                binding.tvDuplicatedIdErrorIndicator.visibility = View.VISIBLE
                            }
                            checkValid = 1
                        } else {//회원탈퇴 이력이 있는 경우 = 탈퇴 후 재가입
                            checkValid = 2
                            //TODO 회원탈퇴 재가입 절차를 밟아보도록 하자
                        }
                    } else {
                        checkValid = 3
                    }

                } else {
                    runOnUiThread {
                        Toast.makeText(this@AccountInPage, "서버 통신 실패", Toast.LENGTH_SHORT)
                            .show()
                    }
                    checkValid = 0
                }
            } catch (_: IOException) {

            }
        }
        checkDuplicatedIdThread.start()
        checkDuplicatedIdThread.join()
        return checkValid
    }


    private fun findNickname(studentId : String,intent: Intent): Int {
        var checkNicknameResponse: Int = -1 //-1 오류, 0 학번에 해당하는 닉네임이 미존재, 1 닉네임이 존재
        val checkSignupThread = Thread {

            val response = RetrofitClient.instance
                .findNickname(JsonParser.parseString(studentId).toString()).execute()

            if (response.isSuccessful) {//닉네임이 존재할 경우
                checkNicknameResponse = 1
                intent.putExtra("nickname", response.body().toString())
                intent.putExtra("studentId", studentId)


            } else {//닉네임이 존재하지 않는 경우
                checkNicknameResponse = 0
            }
        }
        checkSignupThread.start()
        checkSignupThread.join()

        return checkNicknameResponse
    }


    private fun checkValidID(intent: Intent, isSingup:Int):Int{ //name,
        val jsonObject = JSONObject()
        jsonObject.put("id", binding.etStudentIdInput.text.toString())
        jsonObject.put("pw", binding.etStudentPasswordInput.text.toString())
        Log.d("AccountInPageSync_TAG", jsonObject.toString())
//동기통신으로 변경

        val checkValidThread = Thread {


            val response: Response<SejongAuthResponse> =
                RetrofitClient.onlySejongAuth.checkSejong(JsonParser.parseString(jsonObject.toString()))
                    .execute()
            if (response.isSuccessful) {
                if (response.body()?.result?.is_auth.toBoolean()) {
                    var name = response.body()?.result?.body?.name
                    var major = response.body()?.result?.body?.major
                    var id = binding.etStudentIdInput.text.toString()

                    if (isSingup == 1) { //회원가입 페이지에서 온 경우
                        //return 1
                        intent.putExtra("id", id)
                        intent.putExtra("name", name)
                        intent.putExtra("major", major)
                        startActivity(intent)
                        finish()

                    } else {//비번을 잊으셨나요 페이지에서 온 경우 //가입존재여부 판단
                        val fineNicknameReturn = //0은 없음, 1은 있음, -1은 오류
                            findNickname(binding.etStudentIdInput.text.toString(),intent)

                        if(fineNicknameReturn==0){
                            //TODO 일치한 닉네임이 없다고 하고 회원가입 페이지로 안내
                            //TODO 우선 토스트 메시지로 대체

                            runOnUiThread{
                                Toast.makeText(this@AccountInPage,"회원가입X", Toast.LENGTH_SHORT).show()
                            }

                        }
                        else if(fineNicknameReturn==1){ //비번 초기화 페이지로 이동
                            startActivity(intent)
                            finish() //이새끼 안써줘서 스택오버플로우 난듯

                        }
                        else{
                            //TODO 오류
                        }





                    }

                } else {//아디 비번이 틀린경우
                    runOnUiThread {
                        Toast.makeText(
                            this@AccountInPage,
                            "아이디 또는 비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }


            }
            else{
                //통신실패
            }
        }
        checkValidThread.start()
        checkValidThread.join()

/*
비동기통신 삭제

        RetrofitClient.onlySejongAuth.checkSejong(JsonParser.parseString(jsonObject.toString()))
            .enqueue(object : Callback<SejongAuthResponse> {
                override fun onResponse(
                    call: Call<SejongAuthResponse>,
                    response: Response<SejongAuthResponse>
                ) {
                    Log.d("AccountInPageSync_TAG", response.toString())
                    if (response.isSuccessful) {
                        Log.d("AccountInPageSync_TAG", response.body().toString())






                        if (response.body()?.result?.is_auth.toBoolean()) {
                            var name = response.body()?.result?.body?.name
                            var major = response.body()?.result?.body?.major
                            var id = binding.etStudentIdInput.text.toString()

                            if(isSingup==1){ //회원가입 페이지에서 온 경우
                                //return 1
                                intent.putExtra("id", id)
                                intent.putExtra("name",name)
                                intent.putExtra("major",major)
                                startActivity(intent)
                                finish()

                            }
                            else{//비번을 잊으셨나요 페이지에서 온 경우

                                //return 2
                                RetrofitClient.instance.findNickname(binding.etStudentIdInput.text.toString())
                                    .enqueue(object :Callback<String>{
                                        override fun onResponse(
                                            call: Call<String>,
                                            response: Response<String>
                                        ) {
                                            Log.d("AccountInPageSync_TAG", response.toString())
                                            if(response.isSuccessful){//계정이 존재하는 경우
                                                val nickname = response.body().toString()
                                                intent.putExtra("id", id)
                                                intent.putExtra("name",name)
                                                intent.putExtra("major",major)
                                                intent.putExtra("nickname",nickname)
                                                startActivity(intent)
                                                finish()
                                            }
                                            else{//계정이 존재하지 않는 경우
                                                //TODO 계정이 없습니다. 회원가입 페이지로 이동하시겠습니까? 멘트 ㄱㄱ
                                                Toast.makeText(this@AccountInPageSync,"계정이 없습니다.",Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        //find_nickname에 대한 onfailure
                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            //TODO("Not yet implemented")
                                        }

                                    })
                            }





                        } else {//학사정보시스템의 id, 비번이 일치하지 않은 경우
                            Toast.makeText(
                                this@AccountInPageSync,
                                "아이디 또는 비밀번호가 일치하지 않습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {//세종auth의 response가 successful이 아닌경우
                        Log.d("AccountInPage_TAG", "실패")
                    }
                }
                //sejongAuth에 대한 onfailure
                override fun onFailure(call: Call<SejongAuthResponse>, t: Throwable) {
                    Log.d("AccountInPage_TAG", "onFailure")
                }
            })

        */

        return -1//에러표현
    }



}