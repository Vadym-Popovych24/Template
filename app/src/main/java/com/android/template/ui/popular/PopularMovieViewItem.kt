package com.android.template.ui.popular

import com.android.template.data.models.db.MovieEntity
import com.android.template.utils.helpers.convert
import com.android.template.utils.helpers.movieDataFormat
import com.android.template.utils.helpers.simpleDateFormat

data class PopularMovieViewItem(
    val id: Long,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double
) {

    companion object {
        fun mapFrom(movieEntity: MovieEntity): PopularMovieViewItem {
            return PopularMovieViewItem(
                id = movieEntity.id,
                overview = movieEntity.overview,
                posterPath = movieEntity.posterPath,
                releaseDate = convert(movieEntity.releaseDate, simpleDateFormat, movieDataFormat),
                title = movieEntity.title,
                voteAverage = movieEntity.voteAverage
            )
        }
    }
}
