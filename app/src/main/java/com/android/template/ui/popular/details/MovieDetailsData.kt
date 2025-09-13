package com.android.template.ui.popular.details

import com.android.template.data.models.api.response.Genre
import com.android.template.data.models.api.response.MovieDetailsResponse
import com.android.template.data.models.api.response.ProductionCompany

data class MovieDetailsView(
    val genres: List<Genre>,
    val originalLanguage: String,
    val popularity: Double,
    val budget: Int,
    val revenue: Int,
    val originCountry: List<String>,
    val productionCompanies: List<ProductionCompany>,
    val tagline: String?,
    val adult: Boolean,
    val voteCount: Int,
    val status: String,
    val homepage: String?
) {

    companion object {
        fun mapFrom(movieDetailsResponse: MovieDetailsResponse): MovieDetailsView {
            return MovieDetailsView(
                genres = movieDetailsResponse.genres,
                originalLanguage = movieDetailsResponse.originalLanguage,
                popularity = movieDetailsResponse.popularity,
                budget = movieDetailsResponse.budget,
                revenue = movieDetailsResponse.revenue,
                originCountry = movieDetailsResponse.originCountry,
                productionCompanies = movieDetailsResponse.productionCompanies,
                tagline = movieDetailsResponse.tagline,
                adult = movieDetailsResponse.adult,
                voteCount = movieDetailsResponse.voteCount,
                status = movieDetailsResponse.status,
                homepage = movieDetailsResponse.homepage
            )
        }
    }
}
