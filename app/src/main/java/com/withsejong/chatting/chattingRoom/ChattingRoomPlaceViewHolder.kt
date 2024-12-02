package com.withsejong.chatting.chattingRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemChattingRoomPlaceBinding
import com.withsejong.databinding.ItemRecentSearchBinding

class ChattingRoomPlaceViewHolder(val binding:ItemChattingRoomPlaceBinding):RecyclerView.ViewHolder(binding.root) {
    val name = binding.tvPlaceName
    val detail = binding.tvPlaceDetail1
    val img = binding.clo

}