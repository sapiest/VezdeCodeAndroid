package com.vezdecode.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vezdecode.api.base.BaseViewModel
import com.vezdecode.api.base.Resource
import com.vezdecode.data.remote.model.IncidentModel
import com.vezdecode.data.remote.repository.IncidentsRepository
import com.vezdecode.utils.Event
import kotlinx.coroutines.launch

class IncidentsViewModel(private val repository: IncidentsRepository) : BaseViewModel() {
    private val _incidents = MutableLiveData<Resource<List<IncidentModel>>>()
    val incidents: LiveData<Resource<List<IncidentModel>>>
        get() = _incidents

    //Routes
    private val _goToDetailIncidentScreen = MutableLiveData<Event<IncidentModel>>()
    val goToDetailIncidentScreen: LiveData<Event<IncidentModel>>
        get() = _goToDetailIncidentScreen

    fun onClick(post: IncidentModel) {
        _goToDetailIncidentScreen.value = Event(post)
    }

    fun updateIncidents() {
        viewModelScope.launch {
            performGetOperation(_incidents) {
                repository.getIncidents()
            }
        }
    }
}