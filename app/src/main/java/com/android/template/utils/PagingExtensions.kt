package com.android.template.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.template.ui.base.BaseFragment
import com.android.template.utils.custom.paging.PagedList
import com.android.template.utils.custom.paging.asMergedLoadStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

fun <T : Any> BaseFragment<*, *>.setupPagedList(
    pagedList: PagedList, adapter: PagingDataAdapter<T, *>, pagingFlow: Flow<PagingData<T>>,
    attachItemsToListOrEndLoad: (() -> Unit)? = null, attachItemsToList: (() -> Unit)? = null
) {

    pagedList.setAdapter(adapter,
        attachItemsToListOrEndLoad = {
            attachItemsToListOrEndLoad?.invoke()
        },
        attachItemsToList = {
            attachItemsToList?.invoke()
        },
        onError = {
            viewModel.handleError(it)
        })

    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            if (!viewWasRestored) {
                pagingFlow
                    .collectLatest {
                        adapter.submitData(lifecycle, it)
                    }
            }
        }
    }

    lifecycleScope.launch { //scroll up when data loaded
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            adapter.loadStateFlow
                .asMergedLoadStates()
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    if (!viewWasRestored) {
                        (pagedList.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            0,
                            0
                        )
                    }
                }
        }
    }

}