package com.withsejong.chatting.chattingRoom

import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemLeftchattingBinding

class ChattingRoomLeftChattingViewHolder(val binding: ItemLeftchattingBinding):RecyclerView.ViewHolder(binding.root) {
    val msg = binding.tvText
    val nickname = binding.tvNicknameIndicator
}