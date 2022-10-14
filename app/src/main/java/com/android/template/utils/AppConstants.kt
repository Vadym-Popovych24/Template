package com.android.template.utils

object AppConstants {
    const val PREF_NAME = "template_pref"
    private const val BASE_URL = ""
    private const val BASE_URL_LOGIN = ""

    const val ENDPOINT_SERVER_LOGIN = "$BASE_URL_LOGIN/Token"
    const val ENDPOINT_DEVICE_LOGIN = "$BASE_URL/api/accounts/user-device"
    const val ENDPOINT_SIGN_UP = "$BASE_URL_LOGIN/Token/Register"
    const val ENDPOINT_RESET_PASSWORD = "$BASE_URL_LOGIN/Token/ResetPassword"
    const val ENDPOINT_RESET_PASSWORD_GET_CODE = "$BASE_URL_LOGIN/Token/ResetEmailPasswordCode"
    const val ENDPOINT_RESET_PASSWORD_SUBMIT_CODE = "$BASE_URL_LOGIN/Token/ResetPasswordCode"
    const val ENDPOINT_CHANGE_PASSWORD = "$BASE_URL/api/accounts"


    /**
     * Login
     */
    const val SCOPE_LOGIN = "openid offline_access email phone profile template_api"
    const val CLIENT_ID = ""
    const val CLIENT_SECRET = ""
    const val GRANT_TYPE_HEADER = "grant_type"
    const val GRANT_PASSWORD = "password"
    const val GRANT_REFRESH_TOKEN = "refresh_token"


    /**
     * Profile
     */

    const val ENDPOINT_GET_PROFILE = "$BASE_URL/api/profile/{profileId}"
    const val ENDPOINT_UPDATE_PROFILE = "$BASE_URL/api/profile"
    const val ENDPOINT_PROFILE_PICTURE = "$BASE_URL/api/ProfilePicture"


    /**
     * For Pagination
     */
    const val GetContactRequestsCount = 50

}