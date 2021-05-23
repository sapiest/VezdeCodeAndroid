package com.vezdecode.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DescriptionModel(
    val id: Long,
    val description: String
): Parcelable{
    companion object{
        fun createFromIncident(incidentModel: IncidentModel) = DescriptionModel(
            id = incidentModel.ticketId?: 0L,
            description = incidentModel.description?: ""
        )
    }
}