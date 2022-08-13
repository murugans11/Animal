package com.murugan.animal.di

import com.murugan.animal.model.AnimalApiService
import dagger.Component


@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(animalApiService: AnimalApiService)
}