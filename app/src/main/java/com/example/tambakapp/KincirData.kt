package com.example.tambakapp

import android.os.Parcelable
import com.github.mikephil.charting.data.Entry
import kotlinx.parcelize.Parcelize

@Parcelize
data class KincirData(
    var name: String,
    var tambakId: Int,
    var battery: Int,
    var condition: String,
    var connection: String,
    var chartEntries: ArrayList<Entry> = ArrayList<Entry>(
        listOf(
            Entry(0f, 11f),
            Entry(1f, 13f),
            Entry(2f, 18f),
            Entry(3f, 15f),
            Entry(4f, 16f)
        )
    ),
    var viewIsVisible: Boolean = false
): Parcelable
