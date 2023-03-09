package com.android.template.data.prefs

import `in`.co.ophio.secure.vault.Utils
import android.app.Application
import android.util.Base64
import java.io.File
import java.security.SecureRandom
import javax.crypto.spec.SecretKeySpec

class KeyStoreKeyGenerator private constructor(val application: Application) {

    /** File where wrapped symmetric key is stored.  */
    private val fileName = application.applicationContext.packageName
    private val keyFile: File = File(application.filesDir, fileName)

    /**
     * If key is already present then returns it otherwise generates a random key and returns it
     * @return key
     */

    fun loadOrGenerateKeys(): String {
        val wrapper = SecretKeyWrapper()
        // Generate secret key if none exists
        if (!keyFile.exists()) {
            val raw = ByteArray(DATA_KEY_LENGTH)
            SecureRandom().nextBytes(raw)
            val key = SecretKeySpec(raw, "AES")
            val wrapped = wrapper.encrypt(Base64.encodeToString(key.encoded, Base64.NO_PADDING ).toString())
            Utils.writeFully(keyFile, wrapped)
        }

        val wrapped = Utils.readFully(keyFile)
        val unwrapped = wrapper.decrypt(wrapped)
        return Base64.encodeToString(unwrapped, Base64.DEFAULT).toString()
    }

    companion object {
        private const val DATA_KEY_LENGTH = 32
        operator fun get(application: Application): KeyStoreKeyGenerator {
            return KeyStoreKeyGenerator(application)
        }
    }
}