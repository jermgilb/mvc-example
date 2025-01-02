package com.example.mvcexample.domain

import com.example.mvcexample.domain.entity.Filter
import com.example.mvcexample.domain.entity.Product
import com.example.mvcexample.domain.entity.toProduct
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random


interface IStoreRepository {
    fun getProducts(filter: Filter): Flow<List<Product>>

    suspend fun buyProduct(id: Int): StoreResult
    fun getFilters() : Flow<List<Filter>>

    sealed interface StoreResult {
        data object Success : StoreResult
        data class Failure(val errorCode: Int, val errorMessage: String) : StoreResult
    }
}

class StoreRepository @Inject constructor() : IStoreRepository {
    private val mockData: Flow<List<Product>> = flow {
        emit(List(1000) { it.toProduct() }) // Emit the mock data as a flow
    }

    override fun getProducts(filter: Filter): Flow<List<Product>> =
        mockData.map { products ->
            when (filter.filterId) {
                0 -> products
                else -> products.filter { it.type == filter.filterId }
            }
        }

    override suspend fun buyProduct(id: Int): IStoreRepository.StoreResult {
        return if (Random.nextInt(100) > 10) IStoreRepository.StoreResult.Success
        else IStoreRepository.StoreResult.Failure(-1, "simulated error")
    }

    override fun getFilters(): Flow<List<Filter>> =
        flowOf(
            listOf(
                Filter(0, "All"),
                Filter(1, "Books"),
                Filter(2, "Electronics"),
                Filter(3, "Clothing"),
                Filter(4, "Home"),
                Filter(5, "Toys"),
                Filter(6, "Beauty"),
                Filter(7, "Sports"),
                Filter(8, "Automotive"),
                Filter(9, "Groceries"),
                Filter(10, "Health")
            )
        )
}
