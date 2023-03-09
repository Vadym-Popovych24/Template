package com.android.template.data.prefs

//import android.annotation.SuppressLint
import android.util.Log
import java.nio.ByteBuffer
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecretKeyWrapper {
 //   @SuppressLint("GetInstance")
 //   private val mCipher = Cipher.getInstance("AES/GCM/NoPadding")

    private val secret: SecretKey

//    private val secureRandom: SecureRandom = SecureRandom()
//    private val GCM_IV_LENGTH = 12

    private var key = ByteArray(16)
    private var associatedData = byteArrayOf()

    init {

        associatedData = byteArrayOf(0x10.toByte(), 0x77.toByte(), 0x7f.toByte(), 0x79.toByte(), 0x2b.toByte(), 0x5e.toByte(),
            0xa1.toByte(), 0xc3.toByte(), 0xfe.toByte(), 0x11.toByte(), 0x7e.toByte(), 0x9b.toByte(), 0xd8.toByte(), 0xf5.toByte(), 0x4d, 0xad.toByte())

         key = byteArrayOf(0x8c.toByte(), 0x28.toByte(), 0x50.toByte(), 0x43.toByte(), 0x1c.toByte(), 0x06.toByte(),
            0xd6.toByte(), 0x4f.toByte(), 0x2b.toByte(), 0x81.toByte(), 0x13.toByte(), 0x95.toByte(), 0x3b.toByte(), 0x8c.toByte(), 0x2f.toByte(), 0xdc.toByte())
        secret = SecretKeySpec(key, "AES")
    }


/*
    */
/**
     * Wrap a [SecretKey] using the public key assigned to this wrapper.
     * Use [.unwrap] to later recover the original
     * [SecretKey].
     *
     * @return a wrapped version of the given [SecretKey] that can be
     * safely stored on untrusted storage.
     *//*

    fun wrap(key: SecretKey): ByteArray {
        mCipher.init(Cipher.WRAP_MODE, secret)
        return mCipher.wrap(key)
    }

    */
/**
     * Unwrap a [SecretKey] using the private key assigned to this
     * wrapper.
     *
     * @param blob a wrapped [SecretKey] as previously returned by
     * [.wrap].
     *//*

    fun unwrap(blob: ByteArray): SecretKey {
        mCipher.init(Cipher.UNWRAP_MODE, secret)
        return mCipher.unwrap(blob, "AES", Cipher.SECRET_KEY) as SecretKey
    }
*/

  /**
     * Encrypt a plaintext with given key.
     *
     * @param plaintext      to encrypt (utf-8 encoding will be used)
     * @param secretKey      to encrypt, must be AES type, see [SecretKeySpec]
     * @param associatedData optional, additional (public) data to verify on decryption with GCM auth tag
     * @return encrypted message
     * @throws Exception if anything goes wrong
     */

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun encryptO(messageToEncrypt: String): ByteArray {
        val plainBytes = messageToEncrypt.toByteArray()
        /////////////////////////////////////////////////////////////////
        val secureRandom = SecureRandom()
     //   val key = ByteArray(16)
        Log.e("keyHex", key.toHex3())
     //   secureRandom.nextBytes(key)
        Log.e("keyHex2", key.toHex3())
        val secretKey: SecretKey = SecretKeySpec(key, "AES")
        Log.e("keyHex3", key.toHex3())
        val iv = ByteArray(12) //NEVER REUSE THIS IV WITH SAME KEY
        secureRandom.nextBytes(iv)
        Log.e("iv", iv.toHex3())
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val parameterSpec = GCMParameterSpec(128, iv) //128 bit auth tag length
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec)
        if (associatedData != null) {
            cipher.updateAAD(associatedData)
        }
        val cipherText = cipher.doFinal(plainBytes)
        val byteBuffer = ByteBuffer.allocate(4 + iv.size + cipherText.size)
        byteBuffer.putInt(iv.size)
        byteBuffer.put(iv)
        byteBuffer.put(cipherText)
        val cipherMessage = byteBuffer.array()
      //  Arrays.fill(key, 0.toByte()) //overwrite the content of key with zeros
        ///////////////////////////////////////////////////////////////////
       // val decrypted = decryptO(cipherMessage, key)
        return cipherMessage
    }

    fun ByteArray.toHex3(): String = joinToString(" ") {
        java.lang.Byte.toUnsignedInt(it).toString(radix = 16).padStart(2, '0')
    }

    //public static decrypt method
    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
     fun decryptO(
        cipherMessage: ByteArray
    ): ByteArray {
        val byteBuffer = ByteBuffer.wrap(cipherMessage)
        val ivLength = byteBuffer.int
        require(!(ivLength < 12 || ivLength >= 16)) {  // check input parameter
            "invalid iv length"
        }
        val iv = ByteArray(ivLength)
        byteBuffer[iv]
        val cipherText = ByteArray(byteBuffer.remaining())
        byteBuffer[cipherText]
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(key, "AES"),
            GCMParameterSpec(128, iv)
        )
        if (associatedData != null) {
            cipher.updateAAD(associatedData)
        }
    //    cipher.update(cipherText)
        return cipher.doFinal(cipherText)
    }

}