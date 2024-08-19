package com.example.texttranslater.di

import com.example.texttranslater.domain.repository.Repository
import com.example.texttranslater.presentation.viewmodel.MainViewModel
import org.koin.dsl.module

val appModule= module {
    single { Repository() }
    single { MainViewModel(get()) }
}