package com.murugan.animal

import com.murugan.animal.di.ApiModule
import com.murugan.animal.model.AnimalApiService

class ApiModuleTest(private val mockApiService: AnimalApiService) : ApiModule() {

    override fun provideAnimalApiService(): AnimalApiService {
        return mockApiService
    }
}