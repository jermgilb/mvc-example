package com.example.mvcexample.domain.entity

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val type: Int
)


fun Int.toProduct() =
    Product(
        id = this,
        title = "Title #$this",
        description = "Description #$this",
        price = BigDecimal(Random.nextDouble(1000.0))
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble(),
        type = (1..10).random()
    )
