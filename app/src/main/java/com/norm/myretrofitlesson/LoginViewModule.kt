package com.norm.myretrofitlesson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModule : ViewModel() {
    val token = MutableLiveData<String>()
}