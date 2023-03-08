package com.android.template.data.prefs

import android.annotation.SuppressLint
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class SecretKeyWrapper {
    @SuppressLint("GetInstance")
    private val mCipher = Cipher.getInstance("AES/GCM/NoPadding")

    private val secret: SecretKey

    init {

        val bytes = byteArrayOf(0xa0.toByte(), 0x80.toByte(), 0xf7.toByte(), 0x74.toByte(), 0x5e.toByte(), 0x94.toByte(),
            0xa2.toByte(), 0xf4.toByte(), 0xe4.toByte(), 0xcc.toByte(), 0xcc.toByte(), 0xab.toByte(), 0xdd.toByte(), 0xaf.toByte(), 0x5d, 0xaf.toByte())
        secret = SecretKeySpec(bytes, "AES")
    }


    /**
     * Wrap a [SecretKey] using the public key assigned to this wrapper.
     * Use [.unwrap] to later recover the original
     * [SecretKey].
     *
     * @return a wrapped version of the given [SecretKey] that can be
     * safely stored on untrusted storage.
     */
    fun wrap(key: SecretKey): ByteArray {
        mCipher.init(Cipher.WRAP_MODE, secret)
        return mCipher.wrap(key)
    }

    /**
     * Unwrap a [SecretKey] using the private key assigned to this
     * wrapper.
     *
     * @param blob a wrapped [SecretKey] as previously returned by
     * [.wrap].
     */
    fun unwrap(blob: ByteArray): SecretKey {
        mCipher.init(Cipher.UNWRAP_MODE, secret)
        return mCipher.unwrap(blob, "AES", Cipher.SECRET_KEY) as SecretKey
    }
}