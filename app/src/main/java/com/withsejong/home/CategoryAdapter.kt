package com.withsejong.home

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.databinding.ItemCategoryBinding
import android.graphics.Color
import com.withsejong.R


class CategoryAdapter(val categoryList: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var categoryClickListener: OnItemClickListener

    private var selectedPosition: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val currentPosition = holder.adapterPosition // 동적으로 현재 위치 가져오기

            // 유효한 위치인지 확인 (NO_POSITION인 경우 무시)
            if (currentPosition != RecyclerView.NO_POSITION) {
                holder.categoryName.text = categoryList[currentPosition]

                // 선택된 항목과 나머지 항목의 배경색 및 텍스트 색상 설정
                if (currentPosition == selectedPosition) {
                    holder.itemView.setBackgroundResource(R.drawable.design_item_category_selected) // 선택된 항목: 검정 배경
                    holder.categoryName.setTextColor(Color.parseColor("#ffffff")) // 선택된 항목: 흰색 텍스트
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.design_item_category) // 기본: 흰색 배경
                    holder.categoryName.setTextColor(Color.parseColor("#46515B")) // 기본: 검정 텍스트
                }

                // 첫 번째 아이템에만 마진 추가
                if (currentPosition == 0) {
                    val firstItemAddMarginStart =
                        holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
                    firstItemAddMarginStart.marginStart = 15.dpToPx(holder.itemView.context)
                    firstItemAddMarginStart.marginEnd = 10.dpToPx(holder.itemView.context)
                } else {
                    val itemAddMarginStart =
                        holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
                    itemAddMarginStart.marginStart = 10.dpToPx(holder.itemView.context)
                    itemAddMarginStart.marginEnd = 10.dpToPx(holder.itemView.context)
                }

                // 클릭 이벤트 처리
                holder.itemView.setOnClickListener {
                    val previousSelectedPosition = selectedPosition
                    selectedPosition = currentPosition

                    // 변경된 항목만 업데이트
                    notifyItemChanged(previousSelectedPosition)
                    notifyItemChanged(selectedPosition)

                    categoryClickListener.onClick(it, currentPosition)
                }
            }
        }
    }

    fun setCategoryClickListener(onCategoryClickListener: OnItemClickListener) {
        this.categoryClickListener = onCategoryClickListener
    }
}

fun Int.dpToPx(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
).toInt()



