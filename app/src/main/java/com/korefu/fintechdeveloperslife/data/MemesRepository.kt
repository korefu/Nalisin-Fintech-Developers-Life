package com.korefu.fintechdeveloperslife.data

import com.korefu.fintechdeveloperslife.data.model.MemeModel
import com.korefu.fintechdeveloperslife.data.model.SortingType
import com.korefu.fintechdeveloperslife.utils.Resource
import io.reactivex.Single

interface MemesRepository {
    val pageSize: Int
    fun getMemesFromServer(sortingType: SortingType, page: Int): Single<Resource<List<MemeModel>>>
}
