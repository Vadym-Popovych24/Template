package com.android.template.data.repository.impl

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.android.template.data.local.interfaces.MoviesStorage
import com.android.template.data.models.api.response.Movie
import com.android.template.data.models.api.response.MovieDetailsResponse
import com.android.template.data.models.db.MovieEntity
import com.android.template.data.remote.api.ApiEndpoints
import com.android.template.data.remote.interfaces.MovieWebservice
import com.android.template.data.repository.interfaces.MovieRepository
import com.android.template.ui.base.paging.RxPagingRemoteMediator
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val storage: MoviesStorage,
    private val webService: MovieWebservice
) : BaseRepositoryImpl(), MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun moviePagingData(): Pager<Int, MovieEntity> = Pager(
        config = PagingConfig(pageSize = ApiEndpoints.PAGINATION_COUNT),
        remoteMediator = RxPagingRemoteMediator(getMovieSourceHandler()),
        pagingSourceFactory = { storage.getMovies() })

    private fun getMovieSourceHandler() =
        object : RxPagingRemoteMediator.SourceHandler<Movie> {

            override fun loadNextBunch(pageNumber: Int) =
                webService.getMovies(pageNumber.toString(), Locale.current.language)

            override fun clearDatabase() = storage.clearMovie()

            override fun insertAll(items: List<Movie>) {
                items.map {
                    MovieEntity.mapFrom(it)
                }.let {
                    storage.saveMovies(it)
                }
            }
        }

    override fun getMovieFromDB(id: Long): LiveData<MovieEntity> =
        storage.getMovieFromDB(id)

    override fun getMovieDetails(id: Long): Single<MovieDetailsResponse> =
        webService.getMovieDetails(id).map {
            MovieEntity.mapFromDetails(it).let { movieEntity ->
                storage.saveMovie(movieEntity)
            }
            it
        }
}