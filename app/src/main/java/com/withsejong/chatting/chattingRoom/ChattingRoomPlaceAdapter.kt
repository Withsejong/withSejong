package com.withsejong.chatting.chattingRoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemChattingRoomPlaceBinding
import com.withsejong.databinding.ItemRecentSearchBinding
import com.withsejong.home.search.SearchRecentWordAdapter.OnItemClickListener

class ChattingRoomPlaceAdapter(val location:ArrayList<Place>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemChattingRoomPlaceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChattingRoomPlaceViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return location.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is ChattingRoomPlaceViewHolder){
            holder.name.text=location[position].name
            holder.detail.text = location[position].detail
            holder.img.setBackgroundResource(location[position].image)
            holder.itemView.setOnClickListener {
                itemClickListener.onClick(it,position)
            }

        }
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}