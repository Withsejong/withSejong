package com.withsejong.chatting.chattingRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemLeftchattingBinding
import com.withsejong.databinding.ItemRightchattingBinding
import com.withsejong.retrofit.LoadingChattingResponse

class ChattingRoomAdapter (val pastChattingList : ArrayList<LoadingChattingResponse>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //1이면 남이쓴 것 0이면 내가쓴 것

        return when (viewType) {
            0 -> {
                val binding = ItemRightchattingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ChattingRoomRightChattingViewHolder(binding)
            }

            else -> {
                val binding = ItemLeftchattingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ChattingRoomLeftChattingViewHolder(binding)
            }
        }

    }

    override fun getItemCount(): Int {
        return pastChattingList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ChattingRoomRightChattingViewHolder){
            holder.msg.text = pastChattingList[position].message
        }
        else if(holder is ChattingRoomLeftChattingViewHolder){
            holder.msg.text = pastChattingList[position].message
            holder.nickname.text = pastChattingList[position].sender
        }
    }

    override fun getItemViewType(position: Int): Int {
        return pastChattingList[position].viewType
    }
}