package com.withsejong.mypage.faq

import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemFaqBinding

class FaqViewHolder(binding: ItemFaqBinding): RecyclerView.ViewHolder(binding.root) {
    val title = binding.tvTitle
    val context = binding.tvContext
}
