package com.gojek.demo.domain.repository

import com.gojek.demo.data.NetworkUtils
import com.gojek.demo.data.model.NetworkResponseWrapper
import com.gojek.demo.di.AppModule
import com.gojek.demo.domain.models.ResponseResource
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import java.lang.Exception
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class BaseRepository @Inject constructor(@AppModule.IoDispatcher var networkDispatcher: CoroutineDispatcher) {

    suspend fun <T> doNetworkCall(networkCall: suspend () -> Response<T>): NetworkResponseWrapper<T> {
        val networkResponse: NetworkResponseWrapper<T>
        try {
            val response = networkCall.invoke()
            if (response.isSuccessful) {
                networkResponse = NetworkResponseWrapper.NetworkSuccess(response.body())
            } else {
                networkResponse = when (response.code()) {
                    NetworkUtils.UNAUTHORIZED ->
                        NetworkResponseWrapper.NetworkError(
                            response.code(),
                            NetworkUtils.unauthorized_err_message
                        )

                    NetworkUtils.SESSION_EXPIRED ->
                        NetworkResponseWrapper.NetworkError(
                            response.code(),
                            NetworkUtils.session_expired_err_message
                        )

                    in 400..499 ->
                        NetworkResponseWrapper.NetworkError(
                            response.code(),
                            NetworkUtils.default_err_message
                        )

                    in 500..599 ->
                        NetworkResponseWrapper.NetworkError(
                            response.code(),
                            NetworkUtils.bad_server_err_message
                        )
                    else -> {
                        NetworkResponseWrapper.NetworkError(
                            response.code(),
                            NetworkUtils.default_err_message
                        )
                    }
                }
            }
        } catch (exception: Exception) {
            return NetworkResponseWrapper.NetworkError(
                NetworkUtils.DEFAULT,
                NetworkUtils.default_err_message
            )
        }
        return networkResponse
    }

    suspend fun <T> executeNetworkCall(networkCallFunction: suspend () -> Response<T>): ResponseResource<T> {
        val networkResponse: NetworkResponseWrapper<T>
        withContext(networkDispatcher) {
            networkResponse = doNetworkCall { networkCallFunction() }
        }
        return parseNetworkResponse(networkResponse)
    }

     suspend fun <T> parseNetworkResponse(networkResponse : NetworkResponseWrapper<T>) : ResponseResource<T> {
        var response : ResponseResource<T>
        response =  when(networkResponse) {
            is NetworkResponseWrapper.NetworkSuccess -> {
                ResponseResource.success(networkResponse.data)
            }
             is NetworkResponseWrapper.NetworkError -> {
                 ResponseResource.error(networkResponse.errorMsg, networkResponse.errorCode)
             }
        }
        return response
    }

}