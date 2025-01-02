package com.example.mvcexample.controller

import com.example.mvcexample.domain.entity.Filter
import com.example.mvcexample.model.StoreModel


class FilterController(
    private val model: StoreModel,
) {
    fun hideList() = model.updateListVisibility(false)
    fun onFilterButtonClick() = model.updateListVisibility(true)
    fun onListItemClick(filter: Filter) = model.updateFilter(filter)
}
