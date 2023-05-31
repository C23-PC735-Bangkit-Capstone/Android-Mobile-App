package com.example.tambakapp.ui.sunting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SuntingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Sunting Fragment"
    }
    val text: LiveData<String> = _text
}