package com.korefu.fintechdeveloperslife

import android.app.Application
import com.korefu.fintechdeveloperslife.di.ApplicationComponent
import com.korefu.fintechdeveloperslife.di.DaggerApplicationComponent

class MyApp : Application() {
    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()
}
