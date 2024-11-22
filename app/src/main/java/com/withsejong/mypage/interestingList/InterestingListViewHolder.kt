package com.withsejong.mypage.interestingList

import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemPostBinding

class InterestingListViewHolder(val binding:ItemPostBinding):RecyclerView.ViewHolder(binding.root) {

    val name = binding.tvBookname
    val price = binding.tvBookprice
    val uploadTime = binding.tvUploadtime
    val booktag = binding.tvBooktag
    val img = binding.vpBookimg
    val postDetail = binding.ibtnPostDetail
    val pick = binding.ibtnPostPick
}