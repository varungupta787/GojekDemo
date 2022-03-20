package com.gojek.demo.data.model

sealed class NetworkResponseWrapper<out T> {
    data class NetworkSuccess<T>(val data:T?): NetworkResponseWrapper<T>()
    data class NetworkError<T>(val errorCode:Int, val errorMsg:String?): NetworkResponseWrapper<T>()
}
