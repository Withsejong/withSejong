package com.withsejong.chatting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemChatBinding
import com.withsejong.retrofit.LoadChattingRoomResponse

class ChattingAdapter(val chatList: ArrayList<LoadChattingRoomResponse>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemClickListener : OnItemClickListener //채팅 클릭
    interface OnItemClickListener {
        fun onClick(v: View, position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChattingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChattingViewHolder) {
            holder.message.text = chatList[position].lastmsg
            holder.nickname.text = "${chatList[position].subscriber}"// - ${chatList[position].boardTitle}"

//            holder.nickname.text = chatList[position].subscriber
//            holder.message.text = chatList[position].

            holder.itemView.setOnClickListener{
                itemClickListener.onClick(it,position)
            }
        }

    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}