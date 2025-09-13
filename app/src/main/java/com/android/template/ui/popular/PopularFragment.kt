package com.android.template.ui.popular

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.template.databinding.FragmentPopularBinding
import com.android.template.ui.base.BaseFragmentWithBottomMenu
import com.android.template.ui.popular.viewmodel.PopularViewModel
import com.android.template.utils.setupPagedList
import javax.inject.Inject

class PopularFragment : BaseFragmentWithBottomMenu<FragmentPopularBinding, PopularViewModel>() {

    @Inject
    lateinit var adapter: PopularMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initDefaultNavigation()

        setupPagedList(binding.pagedList, adapter, viewModel.pagingFlow)

        adapter.onItemClick = {
            PopularFragmentDirections.actionNavigationPopularToMovieDetails(it.id.toString())
                .run {
                    findNavController().navigate(this)
                }
        }
    }
}