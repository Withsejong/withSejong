package com.withsejong.home

import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemPostBinding

//class HomeViewHolder(binding: ItemPostBinding) : RecyclerView.ViewHolder() {
//
//}

class HomeViewHolder(val binding:ItemPostBinding) : RecyclerView.ViewHolder(binding.root){
    val name = binding.tvBookname
    val price = binding.tvBookprice
    val uploadTime = binding.tvUploadtime
    val booktag = binding.tvBooktag
    val img = binding.vpBookimg
    val postDetail = binding.ibtnPostDetail
    val pick = binding.ibtnPostPick




    //TODO 추가 위젯에 대한 변수 정의할 것
}