package com.example.mvcexample.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvcexample.DefaultDispatcher
import com.example.mvcexample.domain.BuyProductUseCase
import com.example.mvcexample.domain.FilterUseCase
import com.example.mvcexample.domain.GetProductsUseCase
import com.example.mvcexample.domain.IStoreRepository
import com.example.mvcexample.domain.entity.Filter
import com.example.mvcexample.domain.entity.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class StoreState(
    val showList: Boolean = false,
    val currentFilter: Filter? = Filter(0, "All"),
)

@HiltViewModel
class StoreModel @Inject constructor(
    filterUseCase: FilterUseCase,
    private val buyProductUseCase: BuyProductUseCase,
    private val productUseCase: GetProductsUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _modelState = MutableStateFlow(StoreState())

    private val _modelEventsFlow = MutableSharedFlow<IStoreRepository.StoreResult?>(0)
    val purchaseResults = _modelEventsFlow.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val viewProductsState: StateFlow<List<Product>?> =
        _modelState
            .mapNotNull { it.currentFilter }
            .flatMapLatest { productUseCase(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    val viewFilterState: StateFlow<Filter?> =
        _modelState.map { it.currentFilter }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    val viewFilterListState: StateFlow<List<Filter>?> =
        combine(
            _modelState,
            filterUseCase()
        ) { state, filters ->
            if (state.showList) filters
            else null
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun buyProduct(product: Product) =
        viewModelScope.launch(defaultDispatcher) {
            buyProductUseCase(product).run { _modelEventsFlow.emit(this) }
        }

    fun updateFilter(filter: Filter) =
        _modelState.update { it.copy(currentFilter = filter) }

    fun updateListVisibility(isVisible: Boolean) =
        _modelState.update { it.copy(showList = isVisible) }
}