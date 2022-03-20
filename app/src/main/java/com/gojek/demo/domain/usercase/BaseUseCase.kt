package com.gojek.demo.domain.usercase

import com.gojek.demo.data.model.NetworkResponseWrapper
import com.gojek.demo.domain.models.ResponseResource

class BaseUseCase<T> {

    suspend fun <T:Any> parseNetworkResponse(networkResponse: NetworkResponseWrapper<T>) : ResponseResource<T> {
        var responseResource : ResponseResource<T>
        responseResource = when(networkResponse) {
            is NetworkResponseWrapper.NetworkSuccess  ->  {
                ResponseResource.success(networkResponse.data)
            }
            is  NetworkResponseWrapper.NetworkError -> {
                ResponseResource.error(networkResponse.errorMsg)
            }
        }
        return responseResource
    }

}