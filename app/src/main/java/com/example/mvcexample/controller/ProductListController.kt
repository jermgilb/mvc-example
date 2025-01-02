package com.example.mvcexample.controller

import com.example.mvcexample.domain.entity.Product
import com.example.mvcexample.model.StoreModel


class ProductListController(
    private val model: StoreModel,
) {
   fun makePurchase(product: Product) = model.buyProduct(product)
}
