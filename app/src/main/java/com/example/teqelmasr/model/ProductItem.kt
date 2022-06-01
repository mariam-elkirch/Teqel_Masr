package com.example.teqelmasr.model
data class ProductItem(
	val products: List<Product>? = null
)

data class ProductPost(
	val product: Product? = null
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
data class Variant(
	val barcode: String? =null,
	val compare_at_price: Any? = null,
	val created_at: String? =null,
	val fulfillment_service: String? =null,
	val grams: Int? = 0,
	val id: Long? = 0,
	val inventory_item_id: Long? = 0,
	val inventory_management: String? = null,
	val inventory_policy: String? = null,
	val inventory_quantity: Int? = 0,
	val option1: String? = null,
	val position: Int? = 0,
	val price: Double? = 0.0,
	val product_id: Long? =0,
	val requires_shipping: Boolean ? = true,
	val sku: String? = null,
	val taxable: Boolean? = true,
	val title: String? = null,
	val updated_at: String? = null,
	val weight: Double? = 0.0,
	val weight_unit: String? = null
)
data class Image(
	val alt: Any? = null,
	val width: Int? = null,
	val variantIds: List<Any?>? = null,
	val height: Int? = null,
	val src: String? =null,
)

data class Product(
	val publishedScope: String? = null,
	val image: Image? = null,
	val bodyHtml: String? = null,

	val images: List<ImagesItem?>? = null,
	val templateSuffix: String? = null,
	val variants: List<Variant>? = null,
	val productType: String? = null,
	val vendor: String? = null,
	val options: List<OptionsItem?>? = null,
	val title: String? = null,
	val status: String? = null,
	val tags: String? = null
)



