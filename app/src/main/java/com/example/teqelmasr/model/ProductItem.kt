package com.example.teqelmasr.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.*

data class ProductItem(
	val products:ArrayList<Product>? = null
)

data class ProductPost(
	val product: Product? = null
)
@Parcelize
@Serializable
data class OptionsItem(
	val name: String? = null,
	val position: Int? = null,
	val product_id : Long? = null
):Parcelable, java.io.Serializable
@Serializable
@Parcelize

data class ImagesItem(
	@Contextual

	val alt:@RawValue  Any? = null,
	val width: Int? = null,
	val src: String? = null,
	val filename: String? = null,
	val attachment: String? = null,
	@SerializedName("variant_ids")
	val variantIds: List<@Contextual @RawValue Any?>? = null,
	val position: Int? = null,
	val height: Int? = null
):Parcelable, java.io.Serializable
@Parcelize
@Serializable

data class Variant(
    val barcode: String? =null,
    @Contextual
	val compare_at_price: @RawValue Any? = null,
    val created_at: String? =null,
    val fulfillment_service: String? =null,
    val grams: Int? = 0,
    val id: Long? = 0,
    val inventory_item_id: Long? = 0,
    val inventory_management: String? = null,
    val inventory_policy: String? = null,
    val inventory_quantity: Int? = 0,
    var option1: String? = null,
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
):Parcelable, java.io.Serializable

@Parcelize
@Serializable
data class Image(
	val alt: @RawValue @Contextual Any? = null,
	val width: Int? = null,
	@SerializedName("variant_ids")
	val variantIds: @RawValue List<@Contextual Any?>? = null,
	val height: Int? = null,
	val filename: String? = null,
	val attachment: String? = null,
	val src: String? =null,

): Parcelable, java.io.Serializable

@Parcelize
@Serializable
data class Product(
	@SerializedName("published_scope")
	val publishedScope: String? = null,
	@Contextual
	val image: Image? = null,
	@SerializedName("body_html")
	val bodyHtml: String? = null,
	val images: @RawValue List<@Contextual ImagesItem?>? = null,
	@SerializedName("template_suffix")
	val templateSuffix: String? = null,
	val variants: List<@Contextual Variant>? = null,
	@SerializedName("product_type")
	val productType: String? = null,
	val vendor: String? = null,
	val options: List<@Contextual OptionsItem?>? = null,
	val updated_at: String? = null,
	val published_at: String? = null,
	val title: String? = null,
	val status: String? = null,
	val tags: String? = null,
	val handle:String? = null
):Parcelable, java.io.Serializable


