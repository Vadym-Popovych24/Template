package com.android.template.data.local

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.android.template.BuildConfig
import com.android.template.data.prefs.PreferencesHelper
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class KeyEncryptor(private val preferences: PreferencesHelper) {

    private val keyStoreAlias = BuildConfig.KEY_ALIAS

    fun getOrCreateEncryptedKey(): ByteArray {

        // Check if we already have an encrypted key stored
        val encryptedKeyBase64 = preferences.getEncryptedDBKey()
        if (encryptedKeyBase64.isNotEmpty()) {
            val encryptedKey = Base64.decode(encryptedKeyBase64, Base64.DEFAULT)
            return decryptKey(encryptedKey)
        }

        // Generate a new random 256-bit key
        val newKey = ByteArray(32).apply { java.security.SecureRandom().nextBytes(this) }

        // Encrypt the key and store it
        val encryptedKey = encryptKey(newKey)
        preferences.setEncryptedDBKey(Base64.encodeToString(encryptedKey, Base64.DEFAULT))

        return newKey
    }

    fun encryptKey(key: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKeystoreKey())

        val iv = cipher.iv
        val encryptedKey = cipher.doFinal(key)

        return iv + encryptedKey // Store IV + encrypted key together
    }

    fun decryptKey(encryptedData: ByteArray): ByteArray {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = keyStore.getKey(keyStoreAlias, null) as SecretKey

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = encryptedData.copyOfRange(0, 12)  // Extract IV
        val encryptedKey = encryptedData.copyOfRange(12, encryptedData.size)  // Extract encrypted key

        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        return cipher.doFinal(encryptedKey)
    }

    fun getOrCreateKeystoreKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

        if (keyStore.containsAlias(keyStoreAlias)) {
            return keyStore.getKey(keyStoreAlias, null) as SecretKey
        }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(
            KeyGenParameterSpec.Builder(keyStoreAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
        )
        return keyGenerator.generateKey()
    }
}