package com.korefu.fintechdeveloperslife.presentation.memes

import androidx.lifecycle.LiveData

interface MemesViewModel {
    val items: LiveData<List<Any>>
    val position: LiveData<Int>
    fun nextItem()
    fun previousItem()
    fun loadMemes()
}
