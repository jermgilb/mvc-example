package com.example.mvcexample.di

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.mvcexample.controller.FilterController
import com.example.mvcexample.controller.ProductListController
import com.example.mvcexample.model.StoreModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ControllerModule {

    @Provides
    fun provideFilterController(
        activity: Activity
    ): FilterController {
        val storeModel: StoreModel = ViewModelProvider(activity as ViewModelStoreOwner)[StoreModel::class.java]
        return FilterController(storeModel)
    }

    @Provides
    fun provideProductListController(
        activity: Activity
    ): ProductListController {
        val storeModel: StoreModel = ViewModelProvider(activity as ViewModelStoreOwner)[StoreModel::class.java]
        return ProductListController(storeModel)
    }
}
