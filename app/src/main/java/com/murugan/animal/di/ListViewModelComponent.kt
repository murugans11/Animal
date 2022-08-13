package com.murugan.animal.di

import com.murugan.animal.viewmodel.ListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, PrefModule::class, AppModule::class])
interface ListViewModelComponent {

    fun inject(listViewModel: ListViewModel)
}