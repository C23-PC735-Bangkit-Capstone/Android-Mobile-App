package com.example.tambakapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TambakData(
    var id: Int,
    var name: String,
    var cycleCount: Int,
    var status: Int,
    var kincirIsVisible: Boolean = false
): Parcelable