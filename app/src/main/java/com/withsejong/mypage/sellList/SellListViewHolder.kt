package com.withsejong.mypage.sellList

import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemSellListBinding

class SellListViewHolder(binding:ItemSellListBinding):RecyclerView.ViewHolder(binding.root) {
    val name = binding.tvBookname
    val price = binding.tvBookprice
    val uploadTime = binding.tvUploadtime
    val booktag = binding.tvBooktag
    val img = binding.vpBookimg
    val postDetail = binding.ibtnPostDetail
    val stateIndicator = binding.tvSellStateIndicator
}