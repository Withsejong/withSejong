package com.withsejong.home

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemCategoryBinding

class CategoryAdapter(val categoryList:ArrayList<String>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var categoryClickListener: OnItemClickListener

    private var lastSelectedPosition:Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }
    interface OnItemClickListener {
        fun onClick(v: View, position:Int)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CategoryViewHolder){
            holder.categoryName.text = categoryList[position]
            if(position==0){
                val firstItemAddMarginStart = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
                firstItemAddMarginStart.marginStart = 15.dpToPx(holder.itemView.context)
            }
            holder.itemView.setOnClickListener {
                categoryClickListener.onClick(it,position)
            }
        }
    }
    fun setCategoryClickListener(onCategoryClickListener: OnItemClickListener){
        this.categoryClickListener=onCategoryClickListener
    }
}
fun Int.dpToPx(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
).toInt()



