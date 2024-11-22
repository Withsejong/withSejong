package com.withsejong.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemRecentSearchBinding
import com.withsejong.home.HomeAdapter

class SearchRecentWordAdapter(val searchWordList : ArrayList<String>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemClickListener : OnItemClickListener //장터에 올라온 책 리스트

    interface OnItemClickListener {
        fun onClick(v: View, position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRecentSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchRecentWordViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return searchWordList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SearchRecentWordViewHolder){
            holder.tv.text = searchWordList[position]
            holder.itemView.setOnClickListener {
                itemClickListener.onClick(it,position)
            }


        }
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}