package com.lior.questionnaire.mvvm.di.module

import com.lior.questionnaire.mvvm.data.repository.MainRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        MainRepository(get())
    }
}