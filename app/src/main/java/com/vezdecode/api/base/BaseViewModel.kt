package com.vezdecode.api.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    suspend fun <T> performGetOperation(
        liveData: MutableLiveData<Resource<T>>,
        networkCall: suspend () -> Resource<T>
    ) {
        liveData.value = Resource.Loading()
        val response = networkCall.invoke()
        if (response.status == Status.SUCCESS) {
            liveData.value = Resource.Success(data = response.data!!)
        } else if (response.status == Status.ERROR) {
            liveData.value = Resource.Error(error = response.error!!)
        }
    }

//    suspend fun <T> performGetEvent(
//        liveData: MutableLiveData<Event<Resource<T?>>>,
//        networkCall: suspend () -> Resource<T>
//    ) {
//        liveData.value = Event(Resource.Loading())
//        val response = networkCall.invoke()
//        if (response.status == Status.SUCCESS) {
//            liveData.value = Event(Resource.Success(response.data))
//        } else if (response.status == Status.ERROR) {
//            liveData.value = Event(Resource.Error(response.error!!))
//        }
//    }
}