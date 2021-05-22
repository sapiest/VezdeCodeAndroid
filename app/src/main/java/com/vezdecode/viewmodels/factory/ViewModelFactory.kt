package com.vezdecode.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vezdecode.api.base.BaseDataSource
import com.vezdecode.data.remote.repository.IncidentsRepository
import com.vezdecode.viewmodels.IncidentsViewModel

class ViewModelFactory<in V : BaseDataSource>(private val params: V) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncidentsViewModel::class.java)) {
            return IncidentsViewModel(params as IncidentsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
