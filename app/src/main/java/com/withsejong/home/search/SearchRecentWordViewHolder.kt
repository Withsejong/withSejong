package com.withsejong.home.search

import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemRecentSearchBinding

class SearchRecentWordViewHolder(val bidning:ItemRecentSearchBinding):RecyclerView.ViewHolder(bidning.root) {

    val tv=bidning.tvRecentSearchName
}