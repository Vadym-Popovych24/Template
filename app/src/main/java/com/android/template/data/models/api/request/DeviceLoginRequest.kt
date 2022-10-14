package com.android.template.data.models.api.request

import android.os.Build
import com.google.gson.annotations.SerializedName

data class DeviceLoginRequest(
    @SerializedName("DeviceId")
    private val deviceId: String,

    @SerializedName("Token")
    private val token: String,

    @SerializedName("Manufacturer")
    private val manufacturer: String = Build.MANUFACTURER,

    @SerializedName("Product")
    private val product: String = Build.PRODUCT,

    @SerializedName("DeviceName")
    private val deviceName: String = Build.DEVICE,

    @SerializedName("Model")
    private val model: String = Build.MODEL,

    @SerializedName("Brand")
    private val brand: String = Build.BRAND
)