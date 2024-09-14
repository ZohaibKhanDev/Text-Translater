package com.example.backgroundremover_changebg.di

import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import org.koin.dsl.module

val appModule= module {
    single { MainViewModel() }
}