package com.android.template.ui.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.android.template.data.remote.api.ApiEndpoints.ENDPOINT_MOVIE_IMAGE
import com.android.template.databinding.ItemMovieBinding
import com.android.template.ui.base.list.BaseViewHolder
import com.android.template.ui.popular.PopularMovieAdapter.PopularMovieViewHolder
import com.android.template.utils.BindingUtils
import javax.inject.Inject

class PopularMovieAdapter @Inject constructor() :
    PagingDataAdapter<PopularMovieViewItem, PopularMovieViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((PopularMovieViewItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PopularMovieViewHolder {
        return ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            PopularMovieViewHolder(this).apply {
                onItemClickCallback = onItemClick
            }
        }
    }

    override fun onBindViewHolder(
        holder: PopularMovieViewHolder, position: Int
    ) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class PopularMovieViewHolder(private val binding: ItemMovieBinding) :
        BaseViewHolder<PopularMovieViewItem>(binding.root) {

        override fun bind(data: PopularMovieViewItem) {
            with(binding) {
                containerMovieContent.setOnClickListener {
                    onItemClickCallback?.invoke(data)
                }
                tvTitle.text = data.title
                tvSubtitle.text = data.overview
                tvDate.text = data.releaseDate
                val average = data.voteAverage.toString()
                tvAverage.text = average
                BindingUtils.setImageUrl(
                    imgMovie, "${ENDPOINT_MOVIE_IMAGE}/${IMAGE_SIZE}/${data.posterPath}"
                )
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<PopularMovieViewItem> =
            object : DiffUtil.ItemCallback<PopularMovieViewItem>() {

                override fun areItemsTheSame(
                    oldValue: PopularMovieViewItem, newValue: PopularMovieViewItem
                ): Boolean {
                    return oldValue.id == newValue.id
                }

                override fun areContentsTheSame(
                    oldValue: PopularMovieViewItem, newValue: PopularMovieViewItem
                ): Boolean {
                    return oldValue.title == newValue.title && oldValue.releaseDate == newValue.releaseDate && oldValue.posterPath == newValue.posterPath
                }
            }

        private const val IMAGE_SIZE = "w500"
    }
}