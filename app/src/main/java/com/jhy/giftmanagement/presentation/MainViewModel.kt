package com.jhy.giftmanagement.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is a blank screen."
    }
    val text: LiveData<String> = _text
}