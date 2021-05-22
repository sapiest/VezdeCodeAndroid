package com.vezdecode.data.remote.repository
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vezdecode.api.base.BaseDataSource
import com.vezdecode.api.base.Resource
import com.vezdecode.data.remote.model.IncidentModel
import com.vezdecode.utils.Constants
import kotlinx.coroutines.delay


class IncidentsRepository : BaseDataSource() {
    suspend fun getIncidents() = liveData<Resource<List<IncidentModel>>> {
        emit(Resource.Loading())
        val rawData = loadFromCDCard(Constants.INCIDENTS_FILENAME)
        delay(1000)
        if (rawData != null) {
            val type = object : TypeToken<List<IncidentModel>>() {}.type
            val data = Gson().fromJson<List<IncidentModel>>(rawData, type)
            emit(Resource.Success(data))
        } else {
            emit(Resource.Error(NullPointerException()))
        }
    }
}