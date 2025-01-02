package com.example.mvcexample.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import com.example.mvcexample.controller.FilterController
import com.example.mvcexample.controller.ProductListController
import com.example.mvcexample.domain.IStoreRepository
import com.example.mvcexample.model.StoreModel
import com.example.mvcexample.ui.theme.MVCTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MvcActivity : ComponentActivity() {

    @Inject lateinit var productListController: ProductListController
    @Inject lateinit var filterController: FilterController

    private val model: StoreModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //To save precious developer time lets just show a toast for now.
        lifecycle.coroutineScope.launch {
            model.purchaseResults
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    Toast.makeText(
                        this@MvcActivity,
                        when (it) {
                            is IStoreRepository.StoreResult.Failure -> "Failure"
                            IStoreRepository.StoreResult.Success -> "Success"
                            null -> error("unknown result.")
                        },
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        setContent {
            val products by model.viewProductsState.collectAsStateWithLifecycle()
            val filters by model.viewFilterListState.collectAsStateWithLifecycle()
            val currentFilter by model.viewFilterState.collectAsStateWithLifecycle()

            MVCTheme {
                MvcScreenView(
                    isLoading = false,
                    currentFilter = currentFilter,
                    filters = filters,
                    filterController = filterController,
                    products = products,
                    productListController = productListController
                )
            }
        }
    }
}

