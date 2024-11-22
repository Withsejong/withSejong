package com.withsejong.home.search

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.withsejong.databinding.ItemPostBinding
import com.withsejong.home.addCommas
import com.withsejong.retrofit.BoardFindResponseDtoList
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class SearchResultAdapter(val postData: ArrayList<BoardFindResponseDtoList>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener //장터에 올라온 책 리스트
    private lateinit var itemDetailClickListener: OnItemClickListener //점3개 클릭
    interface OnItemClickListener {
        fun onClick(v: View, position:Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchResultViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return postData.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SearchResultViewHolder){
            holder.name.text = postData[position].title
            //원래 가격에 콤마 삽입
            holder.price.text = "${postData[position].price.addCommas()}원"

            if(postData[position].image.isNotEmpty()) {
                Log.d("HomeAdapter_TAG1", postData[position].image[0].url)
                Glide.with(holder.itemView.context)
                    .load(postData[position].image[0].url)
                    .into(holder.img)
            }


            //holder.uploadTime.text = "${postData[position].postTime}분 전"

            for(i:Int in 0 until postData[position].tag.size) {
                if (i == 0) {
                    holder.booktag.text = "#"+postData[position].tag[0].category.toString()
                } else {
                    holder.booktag.text =
                        "${holder.booktag.text}" + " " + "#"+postData[position].tag[i].category.toString()
                }
            }

            //게시글 작성 시간 관련 코드

            //한국시간으로 변환

            val utcTime = postData[position].createdAt.toString()
            val utcDateTime = ZonedDateTime.parse(utcTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

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

            holder.uploadTime.text = elapsedTime

            //클릭 리스너를 달기 위한 코드
            holder.itemView.setOnClickListener{
                itemClickListener.onClick(it,position)
            }
            holder.postDetail.setOnClickListener {


                itemDetailClickListener.onClick(it,position)
            }
        }
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}