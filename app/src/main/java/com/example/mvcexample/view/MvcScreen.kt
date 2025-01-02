package com.example.mvcexample.view

import ProductListComposable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvcexample.controller.FilterController
import com.example.mvcexample.controller.ProductListController
import com.example.mvcexample.domain.entity.Filter
import com.example.mvcexample.domain.entity.Product


@Composable
fun MvcScreenView(
    isLoading: Boolean,
    currentFilter: Filter?,
    filters: List<Filter>?,
    filterController: FilterController,
    products: List<Product>?,
    productListController: ProductListController
) {
    Scaffold(
        floatingActionButton = {
            currentFilter?.let {
                FabButton(currentFilter.title) { filterController.onFilterButtonClick() }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Box(Modifier.padding(it)) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = LocalContentColor.current,
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center),
                    trackColor = LocalContentColor.current.copy(alpha = 0.3f)
                )
            }

            products?.let {
                ProductListComposable(
                    controller = productListController,
                    products = products
                )
            }

            FilterListBottomSheet(
                controller = filterController,
                items = filters
            )
        }
    }
}

@Composable
private fun FabButton(
    message: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = message,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
