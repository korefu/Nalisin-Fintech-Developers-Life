package com.korefu.fintechdeveloperslife.data.api

import com.korefu.fintechdeveloperslife.data.response.EntriesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DevelopersLifeService {
    @GET("{type}/{page}?json=true")
    fun getEntries(
        @Path("type") type: String,
        @Path("page") page: Int,
        @Query("pageSize") pageSize: Int? = null
    ): Single<EntriesResponse>
}
