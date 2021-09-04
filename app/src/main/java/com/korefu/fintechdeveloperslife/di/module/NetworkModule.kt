package com.korefu.fintechdeveloperslife.di.module

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.korefu.fintechdeveloperslife.data.api.DevelopersLifeService
import dagger.Module
import dagger.Provides
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().build()
    }

    @Provides
    fun provideDevelopersLifeService(okHttpClient: OkHttpClient): DevelopersLifeService {
        return Retrofit.Builder()
            .baseUrl("https://developerslife.ru/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DevelopersLifeService::class.java)
    }
}
