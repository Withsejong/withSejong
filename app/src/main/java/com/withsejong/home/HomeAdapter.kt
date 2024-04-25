package com.withsejong.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemPostBinding


fun Int.addCommas(): String {//10000을 10,000으로 바꿔주는 확장함수
    return "%,d".format(this)
}
class HomeAdapter(val postData:ArrayList<PostData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener //장터에 올라온 책 리스트
    private lateinit var itemDetailClickListener: OnItemDetailClickListener //점3개 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemPostBinding = ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position:Int)
    }
    interface OnItemDetailClickListener{
        fun onClick(v:View, position: Int)

    }

    override fun getItemCount(): Int {

        return postData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is HomeViewHolder){
            holder.name.text = postData[position].name
            //원래 가격에 콤마 삽입
            holder.price.text = "${postData[position].price.addCommas()}원"
            holder.uploadTime.text = "${postData[position].postTime}분 전"


            for(i:Int in 0 until postData[position].tag.size) {
                if (i == 0) {
                    holder.booktag.text = postData[position].tag[0]
                } else {
                    holder.booktag.text =
                        "${holder.booktag.text}" + " " + postData[position].tag[i].toString()
                }
            }

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

    fun setItemDetailClickListener(onItemDetailClickListener: OnItemDetailClickListener){
        this.itemDetailClickListener = onItemDetailClickListener
    }
}