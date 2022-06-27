package com.lior.questionnaire.mvvm

import android.app.Application
import com.lior.questionnaire.mvvm.di.module.appModule
import com.lior.questionnaire.mvvm.di.module.repoModule
import com.lior.questionnaire.mvvm.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}
