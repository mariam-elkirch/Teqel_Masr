import com.google.gson.annotations.SerializedName

// To parse the JSON, install Klaxon and do:
//
//   val favouriteProduct = FavouriteProduct.fromJson(jsonString)

data class FavouriteProduct (
    @SerializedName( "draft_order")
    val draftOrder: DraftOrder
)
data class FavProducts(
    @SerializedName( "draft_orders")
    val draftOrders: List<DraftOrder>
    )

data class DraftOrder (
    val email: String,
    val note: String,
    val id : Long? = null,
    @SerializedName( "note_attributes")
    val noteAttributes: List<NoteAttribute>,
    @SerializedName( "line_items")
    val lineItems: List<LineItem>,
    @SerializedName( "customer")
    val customer: favCustomer
)
data class favCustomer (
    val id: Long? = null ,
    val email: String? ,
    val firstName: String? ,
    val lastName: String? = null ,
    val ordersCount: Long? = null ,
    val lastOrderID: Long? = null ,
    val note: String? = null,
    val phone: String? ,
    val tags: String? = null,
    val lastOrderName: String? = null,
    val currency: String? = null,
)

data class LineItem (
    @SerializedName( "product_id")
    val productID: Long,
    val variant_id:Long,
    val taxable: Boolean = false,
    val title: String,
    val quantity: Long,
    val price: String
)

data class NoteAttribute (
    val name: String,
    val value: String?
)
