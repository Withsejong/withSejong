package com.withsejong.home.market

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.withsejong.databinding.ItemPostImgBinding

class PostDetailViewpagerAdapter(val imageList : ArrayList<String>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val binding = ItemPostImgBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PagerViewHolder){
//            Glide.with(holder.itemView.context)
//                .load(imageList[position])
//                .into(holder.iv)
        }


    }
}