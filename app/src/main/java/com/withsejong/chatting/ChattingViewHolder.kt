package com.withsejong.chatting

import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemChatBinding

class ChattingViewHolder(binding:ItemChatBinding):RecyclerView.ViewHolder(binding.root) {
    val nickname = binding.tvSenderNickname
    val message = binding.tvMessage
    val unreadChatting = binding.tvUnreadChattingCntIndicator
}