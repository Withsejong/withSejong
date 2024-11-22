package com.withsejong.home

import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemCategoryBinding

class CategoryViewHolder(binding:ItemCategoryBinding):RecyclerView.ViewHolder(binding.root) {
    val categoryName = binding.tvCategoryName
    

}