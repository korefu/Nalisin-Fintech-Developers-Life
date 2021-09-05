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

class MemesViewModelImpl @AssistedInject constructor(
    private val repository: MemesRepository,
    @Assisted private val sortingType: SortingType
) : ViewModel(), MemesViewModel {
    private val _items = MutableLiveData<List<Any>>()
    override val items: LiveData<List<Any>> = _items
    private val _position = MutableLiveData(0)
    override val position: LiveData<Int> = _position
    private val memes = mutableListOf<MemeModel>()
    private var pagingStatus = PagingStatus.READY
    private var nextPage: Int = 0
    private val compositeDisposable = CompositeDisposable()

    override fun nextItem() {
        val currentPosition = _position.value ?: 0
        _position.value = currentPosition + 1
        if (pagingStatus == PagingStatus.READY && position.value!! >= memes.size - 4)
            loadMemes()
    }

    override fun previousItem() {
        val currentPosition = _position.value ?: 0
        _position.value = currentPosition - 1
    }

    override fun loadMemes() {
        pagingStatus = PagingStatus.LOADING
        updateItems()
        repository.getMemesFromServer(sortingType, nextPage)
            .subscribe({
                if (it.data != null) {
                    nextPage += 1
                    memes.addAll(it.data)
                    pagingStatus =
                        if (it.data.size == repository.pageSize)
                            PagingStatus.READY
                        else PagingStatus.END
                } else
                    pagingStatus = PagingStatus.ERROR
                updateItems()
            }, {
                pagingStatus = PagingStatus.ERROR
                updateItems()
            }).addTo(compositeDisposable)
    }

    private fun updateItems() {
        val newList = memes.plus(pagingStatus)
        _items.value = newList
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    @AssistedFactory
    interface MemesViewModelAssistedFactory {
        fun create(
            sortingType: SortingType
        ): MemesViewModelImpl
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
