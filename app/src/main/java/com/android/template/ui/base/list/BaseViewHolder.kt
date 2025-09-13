package com.android.template.ui.base.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<Any>(itemView: View): RecyclerView.ViewHolder(itemView) {
    var onItemClickCallback: ((Any) -> Unit)? = null

    open fun bind(data: Any) {
        itemView.setOnClickListener {
            onItemClickCallback?.invoke(data)
        }
    }

}