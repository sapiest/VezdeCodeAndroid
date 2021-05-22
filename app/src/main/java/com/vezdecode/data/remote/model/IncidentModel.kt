package com.vezdecode.data.remote.model

import com.google.gson.annotations.SerializedName
import com.vezdecode.utils.DateUtils

data class IncidentModel(
    @SerializedName("STATUS")
    val status: String? = null,
    @SerializedName("TICKETID")
    val ticketId: Long? = null,
    @SerializedName("REPORTEDBY")
    val reportedBy: String? = null,
    @SerializedName("CLASSIDMAIN")
    val classIdMain: String? = null,
    @SerializedName("CRITIC_LEVEL")
    val criticLevel: Int? = null,
    @SerializedName("ISKNOWNERRORDATE")
    private val _isKnowErrorDate: String? = null,
    @SerializedName("TARGETFINISH")
    private val _targetFinish: String? = null,
    @SerializedName("DESCRIPTION")
    val description: String? = null,
    @SerializedName("EXTSYSNAME")
    val extSysName: String? = null,
    @SerializedName("NORM")
    val norm: Double? = null,
    @SerializedName("LNORM")
    val lnorm: Int? = null
) {
    val isKnowErrorDate: String?
        get() = _isKnowErrorDate?.let { DateUtils.getDMYFormat(it) }
    val targetFinish: String?
        get() = _targetFinish?.let { DateUtils.getDMYFormat(it) }
    val ticketIdBrackets: String
        get() = "($ticketId)"
}