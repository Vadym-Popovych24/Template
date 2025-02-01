package com.android.template.data.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.template.data.local.converter.ListIntConverter
import com.android.template.data.models.api.response.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    @ColumnInfo("adult")
    val adult: Boolean,

    @ColumnInfo("backdrop_path")
    val backdropPath: String?,

    @ColumnInfo("original_language")
    val originalLanguage: String,

    @ColumnInfo("original_title")
    val originalTitle: String,

    @ColumnInfo("overview")
    val overview: String,

    @ColumnInfo("popularity")
    val popularity: Double,

    @ColumnInfo("poster_path")
    val posterPath: String?,

    @ColumnInfo("release_date")
    val releaseDate: String,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("video")
    val video: Boolean,

    @ColumnInfo("vote_average")
    val voteAverage: Double,

    @ColumnInfo("vote_count")
    val voteCount: Int,

    @TypeConverters(ListIntConverter::class)
    @ColumnInfo("genre_ids")
    val genreIds: List<Int>
) {

    companion object {

        fun mapFrom(movie: Movie): MovieEntity {
            return MovieEntity(
                id = movie.id,
                adult = movie.adult,
                backdropPath = movie.backdropPath,
                originalLanguage = movie.originalLanguage,
                originalTitle = movie.originalTitle,
                overview = movie.overview,
                popularity = movie.popularity,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                title = movie.title,
                video = movie.video,
                voteAverage = movie.voteAverage,
                voteCount = movie.voteCount,
                genreIds = movie.genreIds
            )
        }

    }
}
