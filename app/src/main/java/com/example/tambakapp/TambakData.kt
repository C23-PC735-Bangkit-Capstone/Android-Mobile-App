package com.example.tambakapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TambakData(
    val pondId: Int,
    val userId: Int,
    val pondLocation: String,
    var kincirViewIsVisible: Boolean = false
) : Parcelable