package com.android.template.ui.base.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

@OptIn(ExperimentalPagingApi::class)
class RxPagingRemoteMediator<T : Any, S : Any>(private val sourceHandler: SourceHandler<S>? = null) :
    RxRemoteMediator<Int, T>() {

    private var page = 1

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, T>
    ): Single<MediatorResult> {
        sourceHandler ?: return Single.just(MediatorResult.Success(endOfPaginationReached = true))

        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .flatMap {
                val loadKey = when (it) {
                    LoadType.REFRESH -> {
                        page = 1
                        page
                    }
                    LoadType.PREPEND -> INVALID_PAGE
                    LoadType.APPEND -> ++page
                }

                if (loadKey == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    sourceHandler.loadNextBunch(loadKey)
                        .map { items ->
                            if (loadType == LoadType.REFRESH)
                                sourceHandler.clearDatabase()

                            sourceHandler.insertAll(items)
                            items.size < state.config.pageSize
                        }
                        .map<MediatorResult> { endOfPaginationReached ->
                            MediatorResult.Success(
                                endOfPaginationReached = endOfPaginationReached
                            )
                        }
                        .onErrorReturn { error -> MediatorResult.Error(error) }
                }
            }
    }

    companion object {
        const val INVALID_PAGE = -1
    }

    interface SourceHandler<T> {
        fun loadNextBunch(pageNumber: Int): Single<List<T>>

        fun clearDatabase()

        fun insertAll(items: List<T>)
    }
}