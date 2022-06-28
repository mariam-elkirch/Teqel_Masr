package com.example.teqelmasr.displaySparePart.view

import android.widget.Filter
import android.widget.Filter.FilterResults
import com.example.teqelmasr.model.Product

import android.widget.Filterable

interface OnProductClickListener {
    fun onProductClick(product: Product)
    fun onEmptyList(searchKey: String)
    fun onFullList()

}