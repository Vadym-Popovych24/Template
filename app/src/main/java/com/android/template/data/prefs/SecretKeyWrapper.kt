package com.android.template.data.prefs

import java.nio.ByteBuffer
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecretKeyWrapper {

/** @param secretKey to encrypt, must be AES type, see [SecretKeySpec] */
    private var key = ByteArray(16)

/**  associatedData optional, additional (public) data to verify on decryption with GCM auth tag */
    private var associatedData = byteArrayOf()

    private val transformation = "AES/GCM/NoPadding"
    private val aesAlgorithm = "AES"

    init {

        associatedData = byteArrayOf(0x10.toByte(), 0x77.toByte(), 0x7f.toByte(), 0x79.toByte(), 0x2b.toByte(), 0x5e.toByte(),
            0xa1.toByte(), 0xc3.toByte(), 0xfe.toByte(), 0x11.toByte(), 0x7e.toByte(), 0x9b.toByte(), 0xd8.toByte(), 0xf5.toByte(), 0x4d, 0xad.toByte())

         key = byteArrayOf(0x8c.toByte(), 0x28.toByte(), 0x50.toByte(), 0x43.toByte(), 0x1c.toByte(), 0x06.toByte(),
            0xd6.toByte(), 0x4f.toByte(), 0x2b.toByte(), 0x81.toByte(), 0x13.toByte(), 0x95.toByte(), 0x3b.toByte(), 0x8c.toByte(), 0x2f.toByte(), 0xdc.toByte())
    }

  /**
     * Encrypt a plaintext with given key.
     *
     * @param plaintext      to encrypt (utf-8 encoding will be used)
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
    fun encrypt(messageToEncrypt: String): ByteArray {
        val plainBytes = messageToEncrypt.toByteArray()
        val secureRandom = SecureRandom()
        val secretKey: SecretKey = SecretKeySpec(key, aesAlgorithm)

        val iv = ByteArray(12) //NEVER REUSE THIS IV WITH SAME KEY
        secureRandom.nextBytes(iv)

        val cipher = Cipher.getInstance(transformation)
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

        return cipherMessage
    }


    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
     fun decrypt(cipherMessage: ByteArray): ByteArray {
        val byteBuffer = ByteBuffer.wrap(cipherMessage)
        val ivLength = byteBuffer.int
        require(!(ivLength < 12 || ivLength >= 16)) {  // check input parameter
            "invalid iv length"
        }
        val iv = ByteArray(ivLength)
        byteBuffer[iv]
        val cipherText = ByteArray(byteBuffer.remaining())
        byteBuffer[cipherText]
        val cipher = Cipher.getInstance(transformation)
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(key, aesAlgorithm),
            GCMParameterSpec(128, iv)
        )
        if (associatedData != null) {
            cipher.updateAAD(associatedData)
        }
    //    cipher.update(cipherText)
        return cipher.doFinal(cipherText)
    }

}