package com.gojek.demo.ui.viewmodel

import androidx.constraintlayout.motion.utils.ViewState
import androidx.lifecycle.ViewModel
import com.gojek.demo.ui.SingleLiveEvent

open class BaseViewModel : ViewModel (){

    val _viewStateLiveData = SingleLiveEvent<ViewStateType>()
    val viewStateLiveData = _viewStateLiveData

    enum class ViewStateType {
        SUCCESS,
        LOADING,
        ERROR
    }

    fun observeViewStateLiveData() : SingleLiveEvent<ViewStateType> {
        return viewStateLiveData
    }
}