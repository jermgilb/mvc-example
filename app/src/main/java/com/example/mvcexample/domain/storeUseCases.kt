package com.example.mvcexample.domain

import com.example.mvcexample.domain.entity.Filter
import com.example.mvcexample.domain.entity.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FilterUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    operator fun invoke(): Flow<List<Filter>> = repository.getFilters()
}

class GetProductsUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    operator fun invoke(filter: Filter): Flow<List<Product>> =
        repository.getProducts(filter)
}

class BuyProductUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    suspend operator fun invoke(product: Product) = repository.buyProduct(product.id)
}
