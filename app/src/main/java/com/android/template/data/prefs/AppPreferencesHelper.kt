package com.android.template.data.prefs

import `in`.co.ophio.secure.core.ObscuredPreferencesBuilder
import android.content.SharedPreferences
import com.android.template.TemplateApp
import com.android.template.data.models.enums.Language
import com.android.template.di.qualifiers.PreferenceInfo
import javax.inject.Inject

class AppPreferencesHelper @Inject constructor(@PreferenceInfo prefFileName: String) :
    PreferencesHelper {

    private val prefName: String = prefFileName
    private val PREF_KEY_REQUEST_TOKEN = "PREF_KEY_REQUEST_TOKEN"
    private val PREF_KEY_TOKEN = "PREF_KEY_TOKEN"
    private val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
    private val PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME"
    private val PREF_KEY_EMAIL = "PREF_KEY_EMAIL"
    private val PREF_KEY_USER_AVATAR = "PREF_KEY_USER_AVATAR"
    private val PREF_KEY_PROFILE_ID = "PREF_KEY_PROFILE_ID"
    private val PREF_KEY_IS_PUBLIC = "PREF_KEY_IS_PUBLIC"
    private val PREF_UUID = "PREF_UUID"
    private val PREF_FCM_TOKEN = "PREF_FCM_TOKEN"
    private val PREF_MAIN_NOTIIFICATION_CHANNEL_ID = "MAIN_NOTIFICATION_CHANNEL_ID"
    private val PREF_LANGUAGE_CODE = "PREF_LANGUAGE_CODE"
    private val PREF_KEY_NEW_NOTIFICATIONS = "PREF_KEY_NEW_NOTIFICATIONS"

    /**
     * KeyGenerator
     */
    private var sekrt = KeyStoreKeyGenerator[TemplateApp.instance].loadOrGenerateKeys()

    private var preferences = ObscuredPreferencesBuilder()
        .setApplication(TemplateApp.instance)
        .obfuscateValue(true)
        .obfuscateKey(true)
        .setSharePrefFileName(prefName)
        .setSecret(sekrt)
        .createSharedPrefs()

    override fun clearAllPreferences() = preferences.edit().clear().apply()

    override fun getRequestToken(): String? = preferences.getString(PREF_KEY_REQUEST_TOKEN, null)

    override fun setRequestToken(requestToken: String) =
        preferences.edit().putString(PREF_KEY_REQUEST_TOKEN, requestToken).apply()

    override fun getToken() = preferences.getString(PREF_KEY_TOKEN, null)

    override fun setToken(token: String) =
        preferences.edit().putString(PREF_KEY_TOKEN, "Bearer $token").apply()

    override fun getRefreshToken(): String? {
        return preferences.getString(PREF_KEY_REFRESH_TOKEN, null)
    }

    override fun setRefreshToken(refreshToken: String?) {
        preferences.edit().putString(PREF_KEY_REFRESH_TOKEN, refreshToken).apply()
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun getUserName(): String? {
        return preferences.getString(PREF_KEY_USER_NAME, null)
    }

    override fun setUserName(userName: String?) {
        preferences.edit().putString(PREF_KEY_USER_NAME, userName).apply()
    }

    override fun getEmail(): String? {
        return preferences.getString(PREF_KEY_EMAIL, null)
    }

    override fun setEmail(email: String?) {
        preferences.edit().putString(PREF_KEY_EMAIL, email).apply()
    }

    override fun getUserAvatar(): String? {
        return preferences.getString(PREF_KEY_USER_AVATAR, null)
    }

    override fun setUserAvatar(avatar: String?) {
        preferences.edit().putString(PREF_KEY_USER_AVATAR, avatar).apply()
    }

    override fun getProfileId(): Int {
        return preferences.getInt(PREF_KEY_PROFILE_ID, 0)
    }

    override fun setProfileId(profileId: Int) {
        preferences.edit().putInt(PREF_KEY_PROFILE_ID, profileId).apply()
    }

    override fun getIsPublic(): Boolean {
        return preferences.getBoolean(PREF_KEY_IS_PUBLIC, true)
    }

    override fun setIsPublic(isPublic: Boolean) {
        preferences.edit().putBoolean(PREF_KEY_IS_PUBLIC, isPublic).apply()
    }

    override fun getUUID(): String =
        preferences.getString(PREF_UUID, "") ?: ""

    override fun setUUID(uuid: String) =
        preferences.edit().putString(PREF_UUID, uuid).apply()

    override fun getFCMToken(): String =
        preferences.getString(PREF_FCM_TOKEN, "") ?: ""

    override fun setFCMToken(token: String) =
        preferences.edit().putString(PREF_FCM_TOKEN, token).apply()

    override fun getMainNotificationChannelId(): String =
        preferences.getString(PREF_MAIN_NOTIIFICATION_CHANNEL_ID, "").toString()

    override fun setMainNotificationChannelId(id: String) =
        preferences.edit().putString(PREF_MAIN_NOTIIFICATION_CHANNEL_ID, id).apply()

    override fun getLanguageCode(): Int =
        preferences.getInt(PREF_LANGUAGE_CODE, Language.EN.code)

    override fun setLanguageCode(languageCode: Int) =
        preferences.edit().putInt(PREF_LANGUAGE_CODE, languageCode).apply()

    override fun getNewNotificationsCount(): Int =
        preferences.getInt(PREF_KEY_NEW_NOTIFICATIONS, 0)

    override fun setNewNotificationsCount(count: Int) =
        preferences.edit().putInt(PREF_KEY_NEW_NOTIFICATIONS, count)
            .apply()
}