package com.withsejong.home.market

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.withsejong.databinding.ActivityPostDetailBinding
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostDetailBinding
    private var responseTag:String ?= null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val itemType = object : TypeToken<ArrayList<String>>() {}.type

        //TODO nickname,major api되면 연결, TAG구현되면 연결
        val nickname = intent.getStringExtra("nickname")
        val major = intent.getStringExtra("major")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")
        val uploadTime = intent.getStringExtra("createAt")
        Log.d("PostDetailActivity_TAG", uploadTime.toString())

        //val tag = intent.getStringArrayListExtra("tag")
        val tag = intent.getStringExtra("tag")
        val tagJsonToList = GsonBuilder().create().fromJson<ArrayList<String>>(tag,itemType)


        tagJsonToList?.forEach {
            if(responseTag==null){
                responseTag="#$it"
            }
            else{
                responseTag = "$responseTag #$it "

            }
        }


        val productContent =  intent.getStringExtra("productContent")

        //val img1 = intent.getStringExtra("img1")
        val imgArray = intent.getStringArrayListExtra("imgArray")



        binding.vpBookimg.adapter = imgArray?.let { PostDetailViewpagerAdapter(it) }
        binding.vpBookimg.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                //해당하는 것에 맞게 컴포넌트 요소들 settext하기
        binding.tvUpperBooknameIndicator.text = productName
        binding.tvUpperPriceIndicator.text = productPrice
        binding.tvNicknameIndicator.text = nickname
        binding.tvMajorIndicator.text = major
        binding.tvProductName.text = productName
        binding.tvProductPrice.text = productPrice
        binding.tvTagIndicator.text = responseTag
        binding.tvContent.text = productContent
        //uptime 설정

        //게시글 작성 시간 관련 코드

        //한국시간으로 변환


        val utcDateTime = ZonedDateTime.parse(uploadTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        // 한국 시간대로 변환
        val koreaDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"))

        // 변환된 시간 출력
        val koreaTime = koreaDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        //현재 디바이스의 시간

        // 현재 디바이스의 시간을 서울 시간 기준으로 가져오기
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

        // 원하는 포맷으로 변환
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val formattedNow = now.format(formatter)

        val duration = Duration.between(koreaDateTime, now)


        // 차이를 원하는 형식으로 변환
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60

        // 차이를 문자열로 출력
        val elapsedTime = when {
            days > 0 -> "$days 일 전"
            hours > 0 -> "$hours 시간 전"
            minutes > 0 -> "$minutes 분 전"
            else -> "방금 전"
        }

        binding.tvUploadtime.text=elapsedTime





//        Glide.with(this@PostDetailActivity)
//            .load(img1)
////            .error(R.mipmap.ic_launcher) //에러시 띄우는 이미지
////            .listener(object : RequestListener<Drawable> {
////                override fun onResourceReady(
////                    resource: Drawable,
////                    model: Any,
////                    target: Target<Drawable>?,
////                    dataSource: DataSource,
////                    isFirstResource: Boolean
////                ): Boolean {
////                    return false
////                }
////
////                override fun onLoadFailed(
////                    e: GlideException?,
////                    model: Any?,
////                    target: Target<Drawable>,
////                    isFirstResource: Boolean
////                ): Boolean {
////                    Log.e("GlideError", "Error loading image", e)
////                    e?.logRootCauses("GlideError") // 추가 로그 출력
////                    return false // false를 반환하면 error 이미지가 표시됩니다.
////                }
////
////            })
//            .into(binding.ivBookimg)
        Log.d("PostDetailActivity_TAG", imgArray.toString())

        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }
}

