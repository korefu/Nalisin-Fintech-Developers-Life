package com.korefu.fintechdeveloperslife.di.module

import com.korefu.fintechdeveloperslife.data.MemesRepository
import com.korefu.fintechdeveloperslife.data.MemesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface BindsModule {
    @Binds
    fun bindMemesRepository(impl: MemesRepositoryImpl): MemesRepository
}
