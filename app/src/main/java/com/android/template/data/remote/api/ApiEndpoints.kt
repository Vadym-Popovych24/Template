package com.android.template.data.remote.api

object ApiEndpoints {

    const val API_KEY = "272381ea875be07ab116a0c117c7c86b"
    const val BASE_URL = "https://api.themoviedb.org/3/" // TODO BuildConfig.API_SERVER_BASE_URL
    private const val BASE_URL_LOGIN = ""
    const val WEB_URL = "https://www.themoviedb.org/"
    const val NEWS_URL = "https://newsapi.org/v2/"

    const val ENDPOINT_WEB_APPROVE = "${WEB_URL}authenticate/"
    const val ENDPOINT_REQUEST_TOKEN = "authentication/token/new"
    const val ENDPOINT_APPROVE_REQUEST_TOKEN = "authenticate"
    const val ENDPOINT_CREATE_SESSION = "authentication/session/new"
    const val ENDPOINT_GET_ACCOUNT = "account"

    /**
     * News
     */

    const val ENDPOINT_GET_NEWS = "top-headlines?sources=techcrunch&apiKey=3f76680cbec04560bfb536eaf7eb13b6"


    /**
     * For Pagination
     */
    const val GetItemCount = 50
}