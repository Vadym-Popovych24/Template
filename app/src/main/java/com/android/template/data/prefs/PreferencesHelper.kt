package com.android.template.data.prefs

import android.content.SharedPreferences

interface PreferencesHelper {
    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    fun clearAllPreferences()

    fun getRequestToken(): String?
    fun setRequestToken(requestToken: String)

    fun getToken(): String?
    fun setToken(token: String)

    fun getRefreshToken(): String?
    fun setRefreshToken(refreshToken: String?)

    fun setFCMToken(token: String)
    fun getFCMToken(): String

    fun setUUID(uuid: String)
    fun getUUID(): String

    fun setMainNotificationChannelId(id: String)
    fun getMainNotificationChannelId(): String

    fun getUserName(): String?
    fun setUserName(userName: String?)

    fun getEmail(): String?
    fun setEmail(email: String?)

    fun getUserAvatar(): String?
    fun setUserAvatar(avatar: String?)

    fun getProfileId(): Long
    fun setProfileId(profileId: Long)

    fun getIsPublic(): Boolean
    fun setIsPublic(isPublic: Boolean)

    fun setLanguageCode(languageCode: Int)
    fun getLanguageCode(): Int

    fun setNewNotificationsCount(count: Int)
    fun getNewNotificationsCount(): Int

    fun getDBProfileId(): Long
    fun setDBProfileId(localProfileId: Long)
}