package com.vezdecode.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.vezdecode.api.base.Resource
import com.vezdecode.api.base.Status

fun <T> MediatorLiveData<Resource<T>>.updateData(data: LiveData<Resource<T>>){
    this.addSource(data) { response ->
        val status = response?.status
        if (status == Status.SUCCESS || status == Status.ERROR) {
            this.removeSource(data)
        }
        this.value = response
    }
}