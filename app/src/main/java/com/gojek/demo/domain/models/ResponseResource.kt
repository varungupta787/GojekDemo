package com.gojek.demo.domain.models

import androidx.annotation.Nullable

class ResponseResource<T> constructor(val status: StatusType, val data: T?, val message: String?, val code: Int?) {

    enum class StatusType { SUCCESS, ERROR }

    companion object {

        fun <T> success(@Nullable data: T?): ResponseResource<T> {
            return ResponseResource(StatusType.SUCCESS, data, null, null)
        }

        fun <T> error(msg: String?, @Nullable data: T?, @Nullable code: Int?): ResponseResource<T> {
            return ResponseResource(StatusType.ERROR, data, msg, code)
        }

        fun <T> error(msg: String?, @Nullable code: Int?): ResponseResource<T> {
            return error(msg, null, code)
        }

        fun <T> error(msg: String?): ResponseResource<T> {
            return error(msg, null, 1)
        }
    }

    override fun toString(): String {
        return "ResponseResource{" +
                "status=" + status +
                ", message='" + message + '\''.toString() +
                ", data=" + data +
                ", code=" + code +
                '}'.toString()
    }
}