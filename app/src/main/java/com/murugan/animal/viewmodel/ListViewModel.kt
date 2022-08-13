package com.murugan.animal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.murugan.animal.di.AppModule
import com.murugan.animal.di.CONTEXT_APP
import com.murugan.animal.di.DaggerListViewModelComponent
import com.murugan.animal.di.TypeOfQualifier
import com.murugan.animal.model.Animal
import com.murugan.animal.model.AnimalApiService
import com.murugan.animal.model.ApiKey
import com.murugan.animal.utils.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ListViewModel(application: Application) : AndroidViewModel(application) {

    val animal by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private var disposable = CompositeDisposable()

    @Inject
    lateinit var apiService: AnimalApiService

    @Inject
    @field:TypeOfQualifier(CONTEXT_APP)
    lateinit var pref: SharedPreferencesHelper

    private var invalidApiKey = false

    init {
        // DaggerListViewModelComponent.create().inject(this)
        DaggerListViewModelComponent
            .builder()
            .appModule(AppModule(getApplication()))
            .build()
            .inject(this)


    }

    fun refresh() {
        if (animal.value == null) {
            loading.value = true
            val key = pref.getApiKey()
            if (key.isNullOrEmpty()) {
                getApiKey()
            } else {
                getAnimal(key)
            }
        }

    }

    fun hardRefresh() {
        loading.value = true
        getApiKey()
    }

    private fun getApiKey() {
        disposable.add(
            apiService.getApiKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                    object : DisposableSingleObserver<ApiKey>() {
                        override fun onSuccess(apiKey: ApiKey) {
                            if (apiKey.key.isNullOrEmpty()) {
                                loading.value = false
                                loadError.value = true
                            } else {
                                pref.saveApiKey(apiKey.key)
                                getAnimal(apiKey.key)
                            }
                        }

                        override fun onError(error: Throwable) {
                            error.printStackTrace()
                            loading.value = false
                            loadError.value = true
                        }
                    }
                )
        )

    }

    private fun getAnimal(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {

                    override fun onSuccess(animalList: List<Animal>) {
                        loadError.value = false
                        animal.value = animalList
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (!invalidApiKey) {
                            invalidApiKey = true
                            getApiKey()
                        } else {
                            e.printStackTrace()
                            loadError.value = true
                            animal.value = null
                            loading.value = false
                        }

                    }
                }
                )

        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}