package com.korefu.fintechdeveloperslife.di

import com.korefu.fintechdeveloperslife.data.MemesRepository
import com.korefu.fintechdeveloperslife.di.module.BindsModule
import com.korefu.fintechdeveloperslife.di.module.NetworkModule
import com.korefu.fintechdeveloperslife.presentation.memes.MemesViewModelImpl
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [NetworkModule::class, BindsModule::class]
)
@Singleton
interface ApplicationComponent {
    fun memesRepository(): MemesRepository
    fun memesViewModelAssistedFactory(): MemesViewModelImpl.MemesViewModelAssistedFactory
}
