package com.android.template.ui.base.list

import androidx.recyclerview.widget.RecyclerView
import com.android.template.ui.base.list.BaseViewHolder

abstract class BaseListAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    var items: ArrayList<T> = arrayListOf()
    var onItemClick: ((T) -> Unit)? = null

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) = with(holder){
        bind(items[position])
        onItemClickCallback = onItemClick
    }

}