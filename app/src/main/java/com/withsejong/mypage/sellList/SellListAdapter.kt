package com.withsejong.mypage.sellList

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.R
import com.withsejong.databinding.ItemSellListBinding
import com.withsejong.home.addCommas
import com.withsejong.retrofit.BoardFindResponseDtoList

    private val TAG = "SellListAdapter_TAG"
class SellListAdapter(val postData: ArrayList<BoardFindResponseDtoList>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSellListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SellListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SellListViewHolder){
            holder.name.text = postData[position].title
            //원래 가격에 콤마 삽입
            holder.price.text = "${postData[position].price.addCommas()}원"

            if(postData[position].image.isNotEmpty()) {
                Log.d(TAG, postData[position].image[0].url)
//                Glide.with(holder.itemView.context)
//                    .load(postData[position].image[0].url)
//                    .into(holder.img)
            }



            //holder.uploadTime.text = "${postData[position].postTime}분 전"

            for(i:Int in 0 until postData[position].tag.size) {
                if (i == 0) {
                    holder.booktag.text = "#"+postData[position].tag[0].category
                } else {
                    holder.booktag.text =
                        "${holder.booktag.text}" + " " + "#"+postData[position].tag[i].category.toString()
                }
            }
            if(postData[position].status==0){//판매중
                holder.stateIndicator.setBackgroundResource(R.drawable.design_post_state_indicator_selling)
                holder.stateIndicator.text="판매중"

            }
            else if(postData[position].status==1){
                holder.stateIndicator.setBackgroundResource(R.drawable.design_post_state_indicator_reserved)
                holder.stateIndicator.text="예약중"


            }
            else{
                holder.stateIndicator.setBackgroundResource(R.drawable.design_post_state_indicator_sold)
                holder.stateIndicator.text="판매완료"


            }
        }
    }
}