package com.korefu.fintechdeveloperslife.data

import com.korefu.fintechdeveloperslife.data.api.DevelopersLifeService
import com.korefu.fintechdeveloperslife.data.model.MemeModel
import com.korefu.fintechdeveloperslife.data.model.SortingType
import com.korefu.fintechdeveloperslife.data.response.EntriesResponse
import com.korefu.fintechdeveloperslife.utils.Resource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MemesRepositoryImpl @Inject constructor(
    private val service: DevelopersLifeService
) : MemesRepository {
    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }

    override val pageSize = DEFAULT_PAGE_SIZE

    override fun getMemesFromServer(
        sortingType: SortingType,
        page: Int
    ): Single<Resource<List<MemeModel>>> {
        return service.getEntries(sortingType.mapToUrlPath(), page, pageSize)
            .map { Resource.success(it.mapToMemesModelList()) }
            .onErrorReturn { Resource.error(it, null) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun SortingType.mapToUrlPath() = this.name.lowercase(Locale.getDefault())

    private fun EntriesResponse.mapToMemesModelList(): List<MemeModel> {
        return this.result.map {
            MemeModel(
                description = it.description,
                gifURL = it.gifURL
            )
        }
    }
}
