package com.android.template.ui.base.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.template.ui.base.BaseFragment

abstract class BasePagedListFragment<D : ViewDataBinding, V : BasePagedListViewModel> : BaseFragment<D, V>() {

    protected open val emptyContentViewLayoutId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = super.onCreateView(inflater, container, savedInstanceState)
       /* contentView?.findViewById<View>(R.id.emptyContentContainer)?.let { emptyContentContainer ->

            if (emptyContentContainer is ViewGroup) {
                emptyContentViewLayoutId?.let {
                    inflateNoContentView(emptyContentContainer)
                }
            }
        }*/
        return contentView
    }

    private fun inflateNoContentView(parent: ViewGroup) = emptyContentViewLayoutId?.let {
        val view = LayoutInflater.from(parent.context).inflate(it, null)
        view
    }?.let {
        parent.addView(it)
    }

    protected fun setupPagingList(recyclerView: RecyclerView, adapter: PagingDataAdapter<*, *>, swipeRefresh: SwipeRefreshLayout?) {
        lifecycleScope.launchWhenStarted {
            if (!viewWasRestored)
                subscribeToPagedLiveData()
        }

        adapter.addLoadStateListener { loadStates ->
            viewModel.isListEmpty.set(loadStates.append.endOfPaginationReached && adapter.itemCount < 1)

            if (loadStates.refresh != LoadState.Loading) {
                swipeRefresh?.isRefreshing = false
                viewModel.screenInitialized.set(true)
            } else {
                viewModel.screenInitialized.set(swipeRefresh?.isRefreshing ?: false)
            }
        }

        recyclerView.adapter = adapter
        swipeRefresh?.setOnRefreshListener { adapter.refresh() }
    }

    abstract suspend fun subscribeToPagedLiveData()
}