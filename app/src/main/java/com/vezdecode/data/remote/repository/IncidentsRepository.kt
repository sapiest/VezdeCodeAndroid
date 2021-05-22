package com.vezdecode.data.remote.repository

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vezdecode.api.base.BaseDataSource
import com.vezdecode.api.base.Resource
import com.vezdecode.data.remote.model.IncidentModel
import com.vezdecode.utils.Constants
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class IncidentsRepository : BaseDataSource() {
    suspend fun getIncidents(): Resource<List<IncidentModel>> {
        val rawData = loadFromCDCard(Constants.INCIDENTS_FILENAME)
        return if (rawData != null) {
            val type = object : TypeToken<List<IncidentModel>>() {}.type
            val data = Gson().fromJson<List<IncidentModel>>(rawData, type)
            Resource.Success(data)
        } else {
            Resource.Error(NullPointerException())
        }
    }
}