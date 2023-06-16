package com.example.tambakapp

import android.os.Parcelable
import com.github.mikephil.charting.data.Entry
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class KincirData(
    val deviceStatus: Boolean,
    val deviceId: Int,
    val batteryStrength: Int,
    val pondId: Int,
    val paddlewheelCondition: String,
    val signalStrength: Int,
    val monitorStatus: Boolean,
    var kincirViewIsVisible: Boolean = false
): Parcelable
