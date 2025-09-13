package com.android.template.utils.custom.paging

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.template.R
import com.android.template.ui.base.paging.LoadingStateAdapter


class PagedList @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val swipeToRefresh: SwipeRefreshLayout
    val recyclerView: RecyclerView
    private val prependProgress: View
    val emptyLayout: ViewGroup
    private val customEmptyLayoutContainer: ViewGroup
    private val isCustomEmptyLayout: Boolean

    private var skipLoadingStateIfRefreshing = false

    init {
        inflate(context, R.layout.paged_list_layout, this)

        swipeToRefresh = findViewById(R.id.swipeToRefresh)
        recyclerView = findViewById(R.id.recyclerView)
        emptyLayout = findViewById(R.id.empty_layout)
        customEmptyLayoutContainer = findViewById(R.id.custom_empty_layout)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.PagedList, 0, 0)

        val  emptyListTitle = attributes.getString(R.styleable.PagedList_empty_list_title)
        val  emptyListSubtitle= attributes.getString(R.styleable.PagedList_empty_list_subtitle)
        val  emptyListDrawable = attributes.getDrawable(R.styleable.PagedList_empty_list_drawable)
        val  prependLoadingLayout = attributes.getResourceId(R.styleable.PagedList_prepend_loading_layout, R.layout.prepend_progress_bar)
        val  customEmptyLayout = attributes.getResourceId(R.styleable.PagedList_custom_empty_list_layout, 0)

        isCustomEmptyLayout = customEmptyLayout != 0

        if (isCustomEmptyLayout) {
            customEmptyLayoutContainer.addView(LayoutInflater.from(context).inflate(customEmptyLayout, this, false))
        }

        prependProgress = LayoutInflater.from(context).inflate(prependLoadingLayout, this, false).apply {
            isVisible = false
        }

        findViewById<FrameLayout>(R.id.content_layout).addView(prependProgress)

        findViewById<TextView>(R.id.empty_list_title)?.text = emptyListTitle
        findViewById<TextView>(R.id.empty_list_subtitle)?.text = emptyListSubtitle
        findViewById<ImageView>(R.id.empty_list_drawable)?.setImageDrawable(emptyListDrawable)

        attributes.recycle()
    }

    fun setAdapter(
        adapter: PagingDataAdapter<*, *>, attachItemsToList: (() -> Unit),
        attachItemsToListOrEndLoad: (() -> Unit), onError: (Throwable) -> Unit
    ) {

        swipeToRefresh.setOnRefreshListener { adapter.refresh() }

        recyclerView.adapter = adapter.withLoadStateFooter(LoadingStateAdapter())

        adapter.addLoadStateListener { loadState ->
//            if (adapter.itemCount > 0 || emptyLayout.isVisible) //show prepend loading until items or empty layout become visible
//                prependProgress.isVisible = false

          //  show prepend loading only if database is empty and getting items from the server
            prependProgress.isVisible = !swipeToRefresh.isRefreshing && loadState.source.refresh !is LoadState.Loading && loadState.mediator?.refresh is LoadState.Loading && adapter.itemCount == 0

            if(skipLoadingStateIfRefreshing) {
                swipeToRefresh.isRefreshing = loadState.mediator?.refresh is LoadState.Loading
                skipLoadingStateIfRefreshing = swipeToRefresh.isRefreshing
            }

            // after adapter.refresh() fist load state is not loading which is incorrect for turn off swipeToRefresh
            if(swipeToRefresh.isRefreshing && loadState.mediator?.refresh !is LoadState.Loading) {
                skipLoadingStateIfRefreshing = true
            }

            if(!swipeToRefresh.isRefreshing) { // avoid changing visibility when is refreshing
                //            emptyLayout.isVisible = loadState.append.endOfPaginationReached && adapter.itemCount == 0
                if (isCustomEmptyLayout) {
                    customEmptyLayoutContainer.isVisible = loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            adapter.itemCount == 0
                } else {
                    emptyLayout.isVisible = loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            adapter.itemCount == 0
                }
            }

            val error = when { //getting the error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }

            error?.error?.let { throwable -> onError(throwable) }

            // invoke callback when not empty data from source attaches to adapter or when endOfLoading
            if (loadState.source.append.endOfPaginationReached && adapter.itemCount != 0 || loadState.append.endOfPaginationReached) {
                attachItemsToListOrEndLoad.invoke()
            }

            // invoke callback when not empty data from source attaches to adapter
            if (loadState.source.append.endOfPaginationReached && adapter.itemCount != 0) {
                attachItemsToList.invoke()
            }
        }

    }

    fun refresh() {
        swipeToRefresh.post { swipeToRefresh.isRefreshing = true }
    }
}