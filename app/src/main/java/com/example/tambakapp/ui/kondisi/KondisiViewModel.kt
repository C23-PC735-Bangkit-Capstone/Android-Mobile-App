package com.example.tambakapp.ui.kondisi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KondisiViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Kondisi Fragment"
    }
    val text: LiveData<String> = _text
}