package com.withsejong.post

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.databinding.ActivityPostBinding
import com.withsejong.retrofit.MakePostResponse
import com.withsejong.retrofit.RefreshTokenResponse
import com.withsejong.retrofit.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private var imgCnt: Int = 0
    private var urlList = ArrayList<Uri>()
    private val TAG = "PostActivity_TAG"

    lateinit var testUri :Uri
    private var multipartList = ArrayList<MultipartBody.Part>()
    lateinit var jsonBody:RequestBody


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tokenSharedPreferences = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val userInfoSharedPreferences = this.getSharedPreferences("userInfo",
            Context.MODE_PRIVATE
        )

        val saveID = userInfoSharedPreferences.getString("studentId", "Error")
        val saveAccessToken = tokenSharedPreferences.getString("accessToken","Error")
        val saveRefreshToken = tokenSharedPreferences.getString("refreshToken", "Error")
        binding.tvImgCnt.text = "${imgCnt}/3"

        binding.ibtnBack.setOnClickListener {
            finish()
        }
        binding.cloAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }
        binding.tvSave.setOnClickListener {
            if(imgCnt==0){
                Toast.makeText(this,"이미지를 최소 1개는 등록해주세요!", Toast.LENGTH_SHORT).show()
            }
            else{

                val saveFilePaths = ArrayList<String>()
                for(i:Int in 0 until urlList.size){
                    saveFilePaths.add(absolutelyPath(urlList[i],this))
                }

                //
                saveFilePaths.forEach { path ->
                    val file = File(path)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    // 파일 이름을 동적으로 지정합니다. 예를 들어, file.name을 사용할 수 있습니다.
                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                    multipartList.add(body)
                }
                Log.d(TAG+"multipart", multipartList.toString())

                //

                val accessToken = tokenSharedPreferences.getString("accessToken","")
                val studentId = userInfoSharedPreferences.getString("studentId", "")
                val jsonObject = JSONObject()
                jsonObject.put("content",binding.etPostDescription.text.toString())
                jsonObject.put("title", binding.etPostTitle.text.toString())
                jsonObject.put("studentId", studentId)
                jsonObject.put("price", binding.etPostPrice.text.toString())

                //TODO 동기통신으로 가보자구
                val uploadPostThread = Thread{
                    //val jsonBody = RequestBody.create(parse("application/json"),jsonObject)
                    jsonBody =
                        jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

                    val response = RetrofitClient.instance.makePost(accessToken = "Bearer $accessToken",
                        request = jsonBody, file = multipartList).execute()
                    Log.d(TAG, response.toString())

                    //403일 때 코드
                    if(response.code().toString()=="403"){
                        //TODO 토큰 리프레시 하는 api 연결
                        val jsonObjectInfo = JSONObject()
                        jsonObjectInfo.put("studentId", saveID)
                        jsonObjectInfo.put("accessToken", saveAccessToken)
                        jsonObjectInfo.put("refreshToken", saveRefreshToken)



                        RetrofitClient.instance.refreshToken(accessToken = "Bearer $saveAccessToken",JsonParser.parseString(jsonObjectInfo.toString()))
                            .enqueue(object :Callback<RefreshTokenResponse> {
                                override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
                                    if(response.isSuccessful){
                                        Log.d("$TAG,갱신 전", saveAccessToken.toString())
                                        val accessTokenNew = response.body()?.accessToken
                                        val refreshTokenNew = response.body()?.refreshToken
                                        val tokenSharedPreferencesEditor = this@PostActivity.getSharedPreferences("token",Context.MODE_PRIVATE).edit()
                                        tokenSharedPreferencesEditor.putString("accessToken",accessTokenNew)
                                        tokenSharedPreferencesEditor.putString("refreshToken", refreshTokenNew)
                                        tokenSharedPreferencesEditor.apply()
                                        Log.d("$TAG,갱신 후", accessTokenNew.toString())
                                        //TODO 공지사항 다시 받는 코드 추가
                                        resavePost(accessTokenNew.toString(),jsonObject)
                                    }
                                }
                                override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                                    Log.d("FaqFragment_TAG", t.toString())
                                }

                            })
                    }
                    else if(response.isSuccessful){
                        //잘 받아온 경우

                        runOnUiThread {
                            Toast.makeText(this@PostActivity,"게시물 저장 성공,임시코드", Toast.LENGTH_SHORT).show()
                            finish()

                        }
                        Log.d(TAG, response.toString())

                    }
                    else{//통신 안된경우
                        Log.d(TAG, response.toString())
                    }
                }
                uploadPostThread.join()
                uploadPostThread.start()

    //            RetrofitClient.instance.makePost(accessToken = "Bearer $accessToken",
    //                request = JsonParser.parseString(jsonObject.toString()), file = urlList).enqueue(
    //                object : Callback<MakePostResponse> {
    //
    //                    override fun onResponse(
    //                        call: Call<MakePostResponse>,
    //                        response: Response<MakePostResponse>
    //                    ) {
    //                        Log.d(TAG, response.toString())
    //                        if(response.code().toString()=="403"){
    //                            //TODO 토큰 리프레시 하는 api 연결
    //                            val jsonObjectInfo = JSONObject()
    //                            jsonObjectInfo.put("studentId", saveID)
    //                            jsonObjectInfo.put("accessToken", saveAccessToken)
    //                            jsonObjectInfo.put("refreshToken", saveRefreshToken)
    //
    //
    //
    //                            RetrofitClient.instance.refreshToken(accessToken = "Bearer $saveAccessToken",JsonParser.parseString(jsonObjectInfo.toString()))
    //                                .enqueue(object :Callback<RefreshTokenResponse> {
    //                                    override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
    //                                        if(response.isSuccessful){
    //                                            Log.d("$TAG,갱신 전", saveAccessToken.toString())
    //                                            val accessTokenNew = response.body()?.accessToken
    //                                            val refreshToken = response.body()?.refreshToken
    //                                            val tokenSharedPreferencesEditor = this@PostActivity.getSharedPreferences("token",Context.MODE_PRIVATE).edit()
    //                                            tokenSharedPreferencesEditor.putString("accessToken",accessTokenNew)
    //                                            tokenSharedPreferencesEditor.putString("refreshToken", refreshToken)
    //                                            tokenSharedPreferencesEditor.apply()
    //                                            Log.d("$TAG,갱신 후", accessToken.toString())
    //
    //
    //                                            //TODO 공지사항 다시 받는 코드 추가
    //                                            resavePost(accessTokenNew.toString(),jsonObject)
    //                                        }
    //                                    }
    //
    //                                    override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
    //                                        Log.d("FaqFragment_TAG", t.toString())
    //                                    }
    //
    //                                })
    //                        }
    //                    }
    //
    //                    override fun onFailure(call: Call<MakePostResponse>, t: Throwable) {
    //                        Log.d(TAG, t.toString())
    //                    }
    //
    //                })

            }
        }
    }
    //파일의 경로를 절대경로로 변환하는 코드
    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    private val activityResult : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== RESULT_OK && it.data!=null){
            val url: Uri? = it.data!!.data
            testUri = it.data!!.data!!
            urlList.add(url!!)
            //TODO 이미지 반영 함수 작성
            setImage()

//            if(imgCnt==0){
//                Glide.with(this)
//                    .load(url)
//                    .into(binding.ivMainImage)
//                imgCnt++
//                binding.btnAddImage.setText("""이미지추가
//                |${imgCnt}/3
//            """.trimMargin())
//            }
//            else if(imgCnt ==1){
//                Glide.with(this)
//                    .load(url)
//                    .into(binding.ibtnSecondImage)
//                imgCnt++
//
//                binding.btnAddImage.setText("""이미지추가
//                |${imgCnt}/3
//            """.trimMargin())
//            }
//           else{
//                Glide.with(this)
//                    .load(url)
//                    .into(binding.ibtnThirdImage)
//                imgCnt++
//                binding.btnAddImage.setText("""이미지추가
//                |${imgCnt}/3
//            """.trimMargin())
//            }


        }
    }

    fun setImage(){
        when(imgCnt){
            0->{
                Glide.with(this)
                    .load(urlList[0])
                    .into(binding.ivMainImage)
                imgCnt++
                binding.tvImgCnt.text = "${imgCnt}/3"

            }
            1->{
                Glide.with(this)
                    .load(urlList[0])
                    .into(binding.ivMainImage)

                Glide.with(this)
                    .load(urlList[1])
                    .into(binding.ibtnSecondImage)
                imgCnt++
                binding.tvImgCnt.text = "${imgCnt}/3"


            }
            2->{
                Glide.with(this)
                    .load(urlList[0])
                    .into(binding.ivMainImage)

                Glide.with(this)
                    .load(urlList[1])
                    .into(binding.ibtnSecondImage)
                Glide.with(this)
                    .load(urlList[2])
                    .into(binding.ibtnThirdImage)
                imgCnt++
                binding.tvImgCnt.text = "${imgCnt}/3"
                binding.cloAddImage.setBackgroundResource(R.drawable.design_post_addbtn_full)

            }
        }
    }

    //TODO resavePost 함수도 복수개의 이미자가 전송되게 설정할 것!
    private fun resavePost(accessToken:String,jsonObject:JSONObject){
        RetrofitClient.instance.makePost(accessToken = "Bearer $accessToken",
            request = jsonBody, file = multipartList).enqueue(object :Callback<MakePostResponse>{
            override fun onResponse(
                call: Call<MakePostResponse>,
                response: Response<MakePostResponse>
            ) {
                Log.d(TAG, response.toString())
                runOnUiThread {
                    Toast.makeText(this@PostActivity,"게시물 저장 성공,임시코드", Toast.LENGTH_SHORT).show()
                    finish()

                }


            }

            override fun onFailure(call: Call<MakePostResponse>, t: Throwable) {
                Log.d(TAG, t.toString())
            }

        })

    }
}