package com.vezdecode.base

sealed class Resource<T>(
    val status: Status,
    val data: T? = null,
    val error: Throwable? = null
) {

    class Loading<T> : Resource<T>(Status.LOADING)
    class Success<T>(data: T) : Resource<T>(Status.SUCCESS, data = data)
    class Error<T>(error: Throwable) : Resource<T>(Status.ERROR, error = error)

    fun isLoading() = status == Status.LOADING

    fun getErrorMessage() = error?.message

    fun shouldShowErrorMessage() = error != null
}


enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}