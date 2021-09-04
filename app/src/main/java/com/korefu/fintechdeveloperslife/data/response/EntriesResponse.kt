package com.korefu.fintechdeveloperslife.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntriesResponse(
    @SerialName("result") val result: List<EntryDto>,
    @SerialName("totalCount") val totalCount: Int
)

@Serializable
data class EntryDto(
    @SerialName("description") val description: String,
    @SerialName("gifURL") val gifURL: String,
    @SerialName("id") val id: Int,
    @SerialName("votes") val votes: Int,
    @SerialName("author") val author: String,
    @SerialName("date") val date: String,
)
