package com.android.template.ui.popular.details

import com.android.template.R
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import com.android.template.databinding.FragmentMovieDetailsBinding
import com.android.template.databinding.ItemMovieBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.popular.PopularMovieAdapter
import com.android.template.utils.getStringFromResource
import kotlinx.coroutines.launch

class MovieDetailsFragment : BaseFragment<FragmentMovieDetailsBinding, MovieDetailsViewModel>() {

    private var viewHolder: PopularMovieAdapter.PopularMovieViewHolder? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()

        val id = arguments?.getLong(EXTRA_MOVIE_ID)
        viewModel.viewModelScope.launch {
            id?.let { viewModel.getMovieItem(it) }
        }

        id?.let { viewModel.getDetails(it) }

        val itemBinding = ItemMovieBinding.inflate(layoutInflater, binding.containerItem, false)
        viewHolder = PopularMovieAdapter.PopularMovieViewHolder(itemBinding)
        binding.containerItem.addView(itemBinding.root)

        changeItemViewBinding(itemBinding)
        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        viewModel.item.observe(viewLifecycleOwner) { item ->
            viewHolder?.bind(item)
        }

        viewModel.details.observe(viewLifecycleOwner) { details ->
            details?.let { bindDetails(it) }
        }

        viewModel.loadingCallback = {
            requireActivity().runOnUiThread {
                binding.progressBar.isVisible = it
            }
        }
    }

    private fun changeItemViewBinding(itemBinding: ItemMovieBinding) {
        itemBinding.apply {
            viewSeparator.isVisible = false
            val layoutParams = imgMovie.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = 0
            imgMovie.layoutParams = layoutParams
            tvTitle.maxLines = Integer.MAX_VALUE
            tvSubtitle.maxLines = Integer.MAX_VALUE
        }
    }

    private fun bindDetails(detailsView: MovieDetailsView) {
        binding.apply {
            tvGenres.text =
                detailsView.genres.joinToString(separator = ",") { it.name }.replace(",", ", ")
            tvLanguage.text = detailsView.originalLanguage
            tvPopularity.text = buildString { append(detailsView.popularity) }
            tvBudget.text = buildString { append(detailsView.budget).append("$") }
            tvRevenue.text = buildString { append(detailsView.revenue).append("$") }
            tvCountry.text = detailsView.originCountry.joinToString(separator = ", ")
            tvCompanies.text =
                detailsView.productionCompanies.joinToString(separator = ", ") { "${it.name} (${it.originCountry})" }
            tvTagline.text = detailsView.tagline
            tvAdult.text = if (detailsView.adult) {
                R.string.yes.getStringFromResource
            } else {
                R.string.no.getStringFromResource
            }
            tvVoteCount.text = buildString { append(detailsView.voteCount) }
            tvStatus.text = detailsView.status
            tvHomepage.text = detailsView.homepage
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearLiveData()
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        fun newInstance(id: Long) = MovieDetailsFragment().apply {
            arguments = bundleOf(Pair(EXTRA_MOVIE_ID, id))
        }
    }
}