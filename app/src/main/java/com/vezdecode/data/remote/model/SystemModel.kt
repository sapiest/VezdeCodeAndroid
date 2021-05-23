package com.vezdecode.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SystemModel(
    val systemName: String,
    val isActive: Boolean
) : Parcelable {
    companion object {
        fun createFromIncident(incidentModel: IncidentModel) =
            SystemModel(systemName = incidentModel.extSysName ?: "", isActive = false)
    }
}