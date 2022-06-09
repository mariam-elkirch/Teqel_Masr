package com.example.teqelmasr.model

data class CustomerObj(
    val accepts_marketing: Boolean? = null,
    val accepts_marketing_updated_at: String? = null,
    val addresses: List<Addresse>? = null,
    val admin_graphql_api_id: String? = null,
    val created_at: String? = null,
    val currency: String? = null,
    val default_address: DefaultAddress? = null,
    val email: String? = null,
    val first_name: String? = null,
    val id: Long? = null,
    val last_name: String? = null,
    val last_order_id: Any? = null,
    val last_order_name: Any? = null,
    val marketing_opt_in_level: String? = null,
    val multipass_identifier: Any? = null,
    var note: String? = null,
    val orders_count: Int? = null,
    val phone: String? = null,
    val state: String? = null,
    val tags: String? = null,
    val tax_exempt: Boolean? = null,
    val tax_exemptions: List<Any>? = null,
    val total_spent: String? = null,
    val updated_at: String? = null,
    val verified_email: Boolean? = null
)
data class DefaultAddress(
    val address1: String? = null,
    val address2: String? = null,
    val city: String? = null,
    val company: String? = null,
    val country: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val customer_id: Long? = null,
    val default: Boolean? = null,
    val first_name: String? = null,
    val id: Long? = null,
    val last_name: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val province: String? = null,
    val province_code: String? = null,
    val zip: String? = null
)
data class Addresse(
    val address1: String? = null,
    val address2: String? = null,
    val city: String? = null,
    val company: String? = null,
    val country: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val customer_id: Long? = null,
    val default: Boolean? = null,
    val first_name: String? = null,
    val id: Long? = null,
    val last_name: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val province: String? = null,
    val province_code: String? = null,
    val zip: String? = null
)