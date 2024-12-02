package com.withsejong.post

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
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
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private var imgCnt: Int = 0
    private var urlList = ArrayList<Uri>()
    private val TAG = "PostActivity_TAG"

    lateinit var testUri: Uri
    private var multipartList = ArrayList<MultipartBody.Part>()
    lateinit var jsonBody: RequestBody


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tokenSharedPreferences = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val userInfoSharedPreferences = this.getSharedPreferences(
            "userInfo",
            Context.MODE_PRIVATE
        )
        val checkBoxList = arrayListOf(
            binding.cbClassification1,
            binding.cbClassification2,
            binding.cbClassification3,
            binding.cbClassification4,
            binding.cbClassification5,
//            binding.cbClassification6,
//            binding.cbClassification7,
//            binding.cbClassification8,

        )

        val tagList = ArrayList<String>()

        val saveID = userInfoSharedPreferences.getString("studentId", "Error")
        val saveAccessToken = tokenSharedPreferences.getString("accessToken", "Error")
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
            val totalTag = JSONArray()
            if (imgCnt == 0) {
                Toast.makeText(this, "이미지를 최소 1개는 등록해주세요!", Toast.LENGTH_SHORT).show()
            } else if (!checkBoxList.any { it.isChecked }) {
                Toast.makeText(this, "게시글 분류 태그를 선택해주세요!", Toast.LENGTH_SHORT).show()

            } else {

//                val saveFilePaths = ArrayList<String>()
//                for(i:Int in 0 until urlList.size){
//                    saveFilePaths.add(absolutelyPath(urlList[i],this))
//                }
//
//                //
//                saveFilePaths.forEach { path ->
//                    val file = File(path)
//                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//                    // 파일 이름을 동적으로 지정합니다. 예를 들어, file.name을 사용할 수 있습니다.
//                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//                    multipartList.add(body)
//                }
//                Log.d(TAG+"multipart", multipartList.toString())

                // saveFilePaths 초기화 및 파일 압축 후 multipartList 생성
                val saveFilePaths = ArrayList<String>()
                for (i: Int in 0 until urlList.size) {
                    saveFilePaths.add(absolutelyPath(urlList[i], this))
                }

// 압축 후 MultipartBody.Part 생성 및 추가
                val multipartList = mutableListOf<MultipartBody.Part>()
                saveFilePaths.forEach { path ->
                    val originalFile = File(path)
                    val compressedFile = compressImageFile(originalFile)  // 파일 압축
                    //val resizedFile = resizeImageToUnder1MB(originalFile)
                    val requestFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
                    val body =
                        MultipartBody.Part.createFormData("file", compressedFile.name, requestFile)
                    multipartList.add(body)
                }

                Log.d(TAG + "multipart", multipartList.toString())

                //

                val accessToken = "Bearer " + tokenSharedPreferences.getString("accessToken", "")
                val studentId = userInfoSharedPreferences.getString("studentId", "")
                val nickname = userInfoSharedPreferences.getString("nickname", "")


                val jsonObject = JSONObject()
                jsonObject.put("content", binding.etPostDescription.text.toString())
                jsonObject.put("title", binding.etPostTitle.text.toString())
                jsonObject.put("studentId", studentId)
                jsonObject.put("price", binding.etPostPrice.text.toString())
                //jsonObject.put("nickname",nickname)


                //이수구분 태그 array에 추가
                checkBoxList.forEach {
                    if (it.isChecked) {
                        totalTag.put(it.text.toString())
                    }
                }

                //사용자 추가 태그 array에 추가
                tagList.forEach {
                    totalTag.put(it)
                }
                jsonObject.put("tags", totalTag)


                Log.d("PostActivity", totalTag.toString())
                //TODO 동기통신으로 가보자구
                val uploadPostThread = Thread {
                    //val jsonBody = RequestBody.create(parse("application/json"),jsonObject)
                    jsonBody =
                        jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                    Log.d(TAG, "access token = $accessToken")
                    val response = RetrofitClient.instance.makePost(
                        accessToken = accessToken,
                        request = jsonBody, file = multipartList
                    ).execute()
                    Log.d(TAG, response.toString())

                    //403일 때 코드
                    if (response.code().toString() == "403") {

                        val tokenRefreshThread = Thread {
                            val jsonObjectInfo = JSONObject()
                            jsonObjectInfo.put("studentId", saveID)
                            jsonObjectInfo.put("accessToken", saveAccessToken)
                            jsonObjectInfo.put("refreshToken", saveRefreshToken)
                            val response2 = RetrofitClient.instance.refreshToken(
                                accessToken = "Bearer $saveAccessToken",
                                JsonParser.parseString(jsonObjectInfo.toString())
                            ).execute()
                            val tokenSharedPreferencesEditor =
                                this.getSharedPreferences("token", Context.MODE_PRIVATE).edit()
                            val accessToken2 = response2.body()?.accessToken
                            val refreshToken2 = response2.body()?.refreshToken
                            Log.d(TAG, "access token = " + accessToken2)
                            Log.d(TAG, "refresh token = " + refreshToken2)
                            tokenSharedPreferencesEditor.putString("accessToken", accessToken2)
                            tokenSharedPreferencesEditor.putString("refreshToken", refreshToken2)
                            tokenSharedPreferencesEditor.apply()
                            Log.d(TAG, response2.toString())
                            if (response2.isSuccessful) {
                                val response3 = RetrofitClient.instance.makePost(
                                    accessToken = "Bearer $accessToken2",
                                    request = jsonBody, file = multipartList
                                ).execute()
                                if (response3.isSuccessful) {
                                    Log.d(TAG, response3.toString())
                                    runOnUiThread {
                                        Toast.makeText(
                                            this@PostActivity,
                                            "게시물 저장 성공, 임시코드2",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    }
                                    Log.d(TAG, response3.toString())
                                }
                            }
                        }
                        tokenRefreshThread.join()
                        tokenRefreshThread.start()

                        //TODO 토큰 리프레시 하는 api 연결 임시 비활성화
//                        val jsonObjectInfo = JSONObject()
//                        jsonObjectInfo.put("studentId", saveID)
//                        jsonObjectInfo.put("accessToken", saveAccessToken)
//                        jsonObjectInfo.put("refreshToken", saveRefreshToken)
//
//
//
//                        RetrofitClient.instance.refreshToken(accessToken = "Bearer $saveAccessToken",JsonParser.parseString(jsonObjectInfo.toString()))
//                            .enqueue(object :Callback<RefreshTokenResponse> {
//                                override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
//                                    if(response.isSuccessful){
//                                        Log.d("$TAG,갱신 전", saveAccessToken.toString())
//                                        val accessTokenNew = response.body()?.accessToken
//                                        val refreshTokenNew = response.body()?.refreshToken
//                                        val tokenSharedPreferencesEditor = this@PostActivity.getSharedPreferences("token",Context.MODE_PRIVATE).edit()
//                                        tokenSharedPreferencesEditor.putString("accessToken",accessTokenNew)
//                                        tokenSharedPreferencesEditor.putString("refreshToken", refreshTokenNew)
//                                        tokenSharedPreferencesEditor.apply()
//                                        Log.d("$TAG,갱신 후", accessTokenNew.toString())
//                                        //TODO 공지사항 다시 받는 코드 추가
//                                        resavePost(accessTokenNew.toString(),jsonObject)
//                                    }
//                                }
//                                override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
//                                    Log.d("FaqFragment_TAG", t.toString())
//                                }
//
//                            })
                    } else if (response.isSuccessful) {
                        //잘 받아온 경우
                        Log.d("PostActivity", response.toString())

                        runOnUiThread {
                            Toast.makeText(this@PostActivity, "게시글 업로드 성공했습니다.\n아래로 당겨 게시글 새로고침 해주세요.", Toast.LENGTH_SHORT)
                                .show()
                            finish()

                        }
                        Log.d(TAG, response.toString())

                    } else {//통신 안된경우
                        Log.d(TAG, response.toString())
                    }
                }
                uploadPostThread.join()
                uploadPostThread.start()
            }
        }

        //체크박스 관련 코드

        for (checkBox in checkBoxList) {
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    checkBoxList.filter { it != buttonView }.forEach { it.isChecked = false }
                }
            }
        }

        //태그 관련 코드
        binding.etTagInput.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (tagList.size > 5) {
                    Toast.makeText(this, "5개 태그를 넘길 수 없습니다!", Toast.LENGTH_SHORT).show()
                } else {
                    if (binding.etTagInput.text.length > 0) {
                        tagList.add(binding.etTagInput.text.toString())

                        when (tagList.size) {
                            1 -> {
                                binding.tvTag01.text = tagList[tagList.size - 1]
                                binding.etTagInput.setText("")
                                binding.tvTag01.visibility = View.VISIBLE
                            }

                            2 -> {
                                binding.tvTag02.text = tagList[tagList.size - 1]
                                binding.etTagInput.setText("")
                                binding.tvTag02.visibility = View.VISIBLE
                            }

                            3 -> {
                                binding.tvTag03.text = tagList[tagList.size - 1]
                                binding.etTagInput.setText("")
                                binding.tvTag03.visibility = View.VISIBLE
                            }

                            4 -> {
                                binding.tvTag04.text = tagList[tagList.size - 1]
                                binding.etTagInput.setText("")
                                binding.tvTag04.visibility = View.VISIBLE
                            }

                            5 -> {
                                binding.tvTag05.text = tagList[tagList.size - 1]
                                binding.etTagInput.setText("")
                                binding.tvTag05.visibility = View.VISIBLE
                            }
                        }
                    }


                }
                Log.d("PostActivity_TAG", tagList.toString())
                true
            } else {
                false
            }


        }

    }

    //파일의 경로를 절대경로로 변환하는 코드
    fun absolutelyPath(path: Uri?, context: Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    private val activityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
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

    fun setImage() {
        when (imgCnt) {
            0 -> {
                Glide.with(this)
                    .load(urlList[0])
                    .into(binding.ivMainImage)
                imgCnt++
                binding.tvImgCnt.text = "${imgCnt}/3"

            }

            1 -> {
                Glide.with(this)
                    .load(urlList[0])
                    .into(binding.ivMainImage)

                Glide.with(this)
                    .load(urlList[1])
                    .into(binding.ibtnSecondImage)
                imgCnt++
                binding.tvImgCnt.text = "${imgCnt}/3"


            }

            2 -> {
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
    private fun resavePost(accessToken: String, jsonObject: JSONObject) {
        RetrofitClient.instance.makePost(
            accessToken = "Bearer $accessToken",
            request = jsonBody, file = multipartList
        ).enqueue(object : Callback<MakePostResponse> {
            override fun onResponse(
                call: Call<MakePostResponse>,
                response: Response<MakePostResponse>
            ) {
                Log.d(TAG, response.toString())
                runOnUiThread {
                    Toast.makeText(this@PostActivity, "게시물 저장 성공,임시코드", Toast.LENGTH_SHORT).show()
                    finish()

                }


            }

            override fun onFailure(call: Call<MakePostResponse>, t: Throwable) {
                Log.d(TAG, t.toString())
            }

        })


    }

    // 이미지 파일 압축 함수
    fun compressImageFile(imageFile: File): File {
        if (imageFile.length() <= 1_000_000) {
            return imageFile  // 1MB 이하면 압축하지 않음
        }

        // 이미지 디코딩
        var bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

        // EXIF 데이터 읽기
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        // EXIF 데이터에 따라 이미지 회전
        bitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }

        var quality = 100
        val outputStream = ByteArrayOutputStream()
        do {
            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            quality -= 5
        } while (outputStream.size() > 1_000_000 && quality > 5)

        val compressedFile = File(imageFile.parent, "compressed_${imageFile.name}")
        val fos = FileOutputStream(compressedFile)
        fos.write(outputStream.toByteArray())
        fos.flush()
        fos.close()

        return compressedFile
    }

    // 비트맵 회전 함수
    fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun resizeImageToUnder1MB(imageFile: File, maxSizeMB: Int = 1): File {
        val maxSizeBytes = maxSizeMB * 1_000_000  // MB를 바이트로 변환

        // 초기 이미지 디코딩
        var options = BitmapFactory.Options()
        var bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

        var currentWidth = bitmap.width
        var currentHeight = bitmap.height
        var scaleFactor = 1

        while (imageFile.length() > maxSizeBytes) {
            // 비율 계산 (크기를 90%로 줄이기)
            scaleFactor += 1
            currentWidth = bitmap.width / scaleFactor
            currentHeight = bitmap.height / scaleFactor

            // Bitmap 축소
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, currentWidth, currentHeight, true)

            // 압축된 Bitmap을 메모리로 저장
            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            // 기존 bitmap 메모리 해제
            bitmap.recycle()
            bitmap = resizedBitmap

            // 파일 크기 확인
            if (outputStream.size() <= maxSizeBytes) {
                val resizedFile = File(imageFile.parent, "resized_${imageFile.name}")
                FileOutputStream(resizedFile).use { fos ->
                    fos.write(outputStream.toByteArray())
                    fos.flush()
                }
                outputStream.close()
                return resizedFile
            }
        }

        // 최종 파일 생성
        val resizedFile = File(imageFile.parent, "resized_${imageFile.name}")
        FileOutputStream(resizedFile).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
        }

        bitmap.recycle()
        return resizedFile
    }


}