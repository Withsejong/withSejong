package com.withsejong.home

import android.view.InflateException
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemPostBinding
fun Int.addCommas(): String {
    return "%,d".format(this)
}
class HomeAdapter(val postData:ArrayList<PostData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemPostBinding = ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeViewHolder(binding)
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

        }



    }
}