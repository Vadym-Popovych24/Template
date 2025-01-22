package com.android.template.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.template.data.models.enums.Language
import com.android.template.di.qualifiers.PreferenceInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AppPreferencesHelper @Inject constructor(
    @PreferenceInfo prefFileName: String, private val context: Context
) : PreferencesHelper {

    private val prefName: String = prefFileName

    private val prefRequestKeyToken = stringPreferencesKey("PREF_KEY_REQUEST_TOKEN")
    private val prefKeyToken = stringPreferencesKey("PREF_KEY_TOKEN")
    private val prefKeyRefreshToken = stringPreferencesKey("PREF_KEY_REFRESH_TOKEN")
    private val prefKeyUserName = stringPreferencesKey("PREF_KEY_USER_NAME")
    private val prefKeyEmail = stringPreferencesKey("PREF_KEY_EMAIL")
    private val prefKeyUserAvatar = stringPreferencesKey("PREF_KEY_USER_AVATAR")
    private val prefKeyProfileId = longPreferencesKey("PREF_KEY_PROFILE_ID")
    private val prefKeyUUID = stringPreferencesKey("PREF_UUID")
    private val prefKeyFCMToken = stringPreferencesKey("PREF_FCM_TOKEN")
    private val prefKeyMainNotificationChannelId =
        stringPreferencesKey("PREF_MAIN_NOTIIFICATION_CHANNEL_ID")
    private val prefKeyNewNotifications = intPreferencesKey("PREF_KEY_NEW_NOTIFICATIONS")
    private val prefKeyLanguageCode = intPreferencesKey("PREF_LANGUAGE_CODE")
    private val prefKeyLocalProfileId = longPreferencesKey("PREF_KEY_LOCAL_PROFILE_ID")

    // init Data Store
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = prefName)

    override fun clearAllPreferences() {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs.clear()
            }
        }
    }

    override fun getRequestToken(): String? = runBlocking { // Fetches the value synchronously
        context.dataStore.data.first()[prefRequestKeyToken]
    }

    override fun setRequestToken(requestToken: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefRequestKeyToken] = requestToken
            }
        }
    }

    override fun getToken(): String? = runBlocking { // Fetches the value synchronously
        context.dataStore.data.first()[prefKeyToken]
    }

    override fun setToken(token: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyToken] = "Bearer $token"
            }
        }
    }

    override fun getRefreshToken(): String? = runBlocking {
        context.dataStore.data.first()[prefKeyRefreshToken]
    }

    override fun setRefreshToken(refreshToken: String?) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyRefreshToken] = refreshToken ?: ""
            }
        }
    }

    override fun getUserName(): String? = runBlocking {
        context.dataStore.data.first()[prefKeyUserName]
    }

    override fun setUserName(userName: String?) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyUserName] = userName ?: ""
            }
        }
    }

    override fun getEmail(): String? = runBlocking {
        context.dataStore.data.first()[prefKeyEmail]
    }

    override fun setEmail(email: String?) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyEmail] = email ?: ""
            }
        }
    }

    override fun getUserAvatar(): String? = runBlocking {
        context.dataStore.data.first()[prefKeyUserAvatar]
    }

    override fun setUserAvatar(avatar: String?) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyUserAvatar] = avatar ?: ""
            }
        }
    }

    override fun getProfileId(): Long = runBlocking {
        context.dataStore.data.first()[prefKeyProfileId] ?: 0
    }

    override fun setProfileId(profileId: Long) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyProfileId] = profileId
            }
        }
    }

    override fun getUUID(): String = runBlocking {
        context.dataStore.data.first()[prefKeyUUID] ?: ""
    }

    override fun setUUID(uuid: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyUUID] = uuid
            }
        }
    }

    override fun getFCMToken(): String = runBlocking {
        context.dataStore.data.first()[prefKeyFCMToken] ?: ""
    }

    override fun setFCMToken(token: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyFCMToken] = token
            }
        }
    }

    override fun getMainNotificationChannelId(): String = runBlocking {
        context.dataStore.data.first()[prefKeyMainNotificationChannelId] ?: ""
    }

    override fun setMainNotificationChannelId(id: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyMainNotificationChannelId] = id
            }
        }
    }

    override fun getLanguageCode(): Int = runBlocking {
        context.dataStore.data.first()[prefKeyLanguageCode] ?: Language.EN.code
    }

    override fun setLanguageCode(languageCode: Int) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyLanguageCode] = languageCode
            }
        }
    }

    override fun getNewNotificationsCount(): Int = runBlocking {
        context.dataStore.data.first()[prefKeyNewNotifications] ?: 0
    }

    override fun setNewNotificationsCount(count: Int) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyNewNotifications] = count
            }
        }
    }

    override fun getDBProfileId(): Long = runBlocking {
        context.dataStore.data.first()[prefKeyLocalProfileId] ?: 0
    }

    override fun setDBProfileId(localProfileId: Long) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[prefKeyLocalProfileId] = localProfileId
            }
        }
    }
}