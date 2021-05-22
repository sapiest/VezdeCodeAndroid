package com.vezdecode.viewmodels

import androidx.lifecycle.*
import com.vezdecode.base.BaseViewModel
import com.vezdecode.base.Resource
import com.vezdecode.data.remote.model.DescriptionModel
import com.vezdecode.data.remote.model.IncidentModel
import com.vezdecode.data.remote.repository.IncidentsRepository
import com.vezdecode.utils.Event
import com.vezdecode.utils.updateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncidentsViewModel(private val repository: IncidentsRepository) : BaseViewModel() {
    private val _incidents = MediatorLiveData<Resource<List<IncidentModel>>>()
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
        viewModelScope.launch(Dispatchers.IO) {
            _incidents.updateData(repository.getIncidents())
        }
    }
}