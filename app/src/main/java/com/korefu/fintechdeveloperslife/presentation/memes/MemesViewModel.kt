package com.korefu.fintechdeveloperslife.presentation.memes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.korefu.fintechdeveloperslife.data.MemesRepository
import com.korefu.fintechdeveloperslife.data.model.MemeModel
import com.korefu.fintechdeveloperslife.data.model.SortingType
import com.korefu.fintechdeveloperslife.utils.PagingStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class MemesViewModel @AssistedInject constructor(
    private val repository: MemesRepository,
    @Assisted private val sortingType: SortingType
) : ViewModel() {
    val memes = mutableListOf<MemeModel>()
    val items = MutableLiveData<List<Any>>()
    var position: Int = 0
    private val _pagingStatus = MutableLiveData<PagingStatus>()
    val pagingStatus: LiveData<PagingStatus> = _pagingStatus
    private var nextPage: Int = 0
    private val compositeDisposable = CompositeDisposable()

    fun loadMemes() {
        _pagingStatus.value = PagingStatus.LOADING
        updateItems()
        repository.getMemesFromServer(sortingType, nextPage)
            .subscribe({
                if (it.data != null) {
                    nextPage += 1
                    memes.addAll(it.data)
                    _pagingStatus.value =
                        if (it.data.size == repository.pageSize)
                            PagingStatus.READY
                        else PagingStatus.END
                } else
                    _pagingStatus.value = PagingStatus.ERROR
                updateItems()
            }, {
                _pagingStatus.value = PagingStatus.ERROR
                updateItems()
            }).addTo(compositeDisposable)
    }

    private fun updateItems() {
        pagingStatus.value?.let {
            val newList = memes.plus(it)
            items.value = newList
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    @AssistedFactory
    interface MemesViewModelAssistedFactory {
        fun create(
            sortingType: SortingType
        ): MemesViewModel
    }

    class Factory(
        private val assistedFactory: MemesViewModelAssistedFactory,
        private val sortingType: SortingType
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return assistedFactory.create(sortingType) as T
        }
    }
}
