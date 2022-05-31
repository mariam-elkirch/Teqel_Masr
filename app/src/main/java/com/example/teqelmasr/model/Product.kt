package com.example.teqelmasr.model


data class ProductItem(
    val products: List<Product>
)

data class OptionsItem(
    val name: String? = null,
    val position: Int? = null
)

data class ImagesItem(
    val alt: Any? = null,
    val width: Int? = null,
    val variantIds: List<Any?>? = null,
    val position: Int? = null,
    val height: Int? = null
)

data class Image(
    val alt: Any? = null,
    val width: Int? = null,
    val variantIds: List<Any?>? = null,
    val height: Int? = null
)

data class Product(
    val publishedScope: String? = null,
    val image: Image? = null,
    val bodyHtml: String? = null,
    val images: List<ImagesItem?>? = null,
    val templateSuffix: String? = null,
    val productType: String? = null,
    val vendor: String? = null,
    val options: List<OptionsItem?>? = null,
    val title: String? = null,
    val status: String? = null,
    val tags: String? = null
)

