package com.example.teqelmasr.displaySellerProducts.view

import com.example.teqelmasr.model.Product

interface OnBtnListener {
    fun onDeleteClick(product: Product, listSize: Int)
    fun onDetailsClick(product: Product)
    fun onEditClick(product: Product)
}