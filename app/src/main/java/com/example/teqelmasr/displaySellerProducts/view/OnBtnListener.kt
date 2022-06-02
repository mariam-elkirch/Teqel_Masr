package com.example.teqelmasr.displaySellerProducts.view

import com.example.teqelmasr.model.Product

interface OnBtnListener {
    fun onDeleteClick(product: Product)
    fun onDetailsClick(product: Product)
}