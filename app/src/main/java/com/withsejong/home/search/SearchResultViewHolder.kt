package com.withsejong.home.search

import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemPostBinding

class SearchResultViewHolder(binding :ItemPostBinding):RecyclerView.ViewHolder(binding.root) {
    val name = binding.tvBookname
    val price = binding.tvBookprice
    val uploadTime = binding.tvUploadtime
    val booktag = binding.tvBooktag
    val img = binding.vpBookimg
    val postDetail = binding.ibtnPostDetail

}