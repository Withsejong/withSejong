package com.withsejong.mypage.faq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemFaqBinding
import com.withsejong.retrofit.loadFaqResponse

class FaqAdapter(val faqlist:ArrayList<loadFaqResponse>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FaqViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return faqlist?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is FaqViewHolder){
            holder.title.text= faqlist?.get(position)?.title ?:"Error"
            holder.context.text  = faqlist?.get(position)?.context ?: "Error"
        }
    }
}