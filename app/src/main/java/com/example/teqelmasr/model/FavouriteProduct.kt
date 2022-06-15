import com.google.gson.annotations.SerializedName

// To parse the JSON, install Klaxon and do:
//
//   val favouriteProduct = FavouriteProduct.fromJson(jsonString)

data class FavouriteProduct (
    @SerializedName( "draft_order")
    val draftOrder: DraftOrder
)

data class DraftOrder (
    val email: String,
    val note: String,
    val id : Long? = null,

    @SerializedName( "note_attributes")
    val noteAttributes: List<NoteAttribute>,

    @SerializedName( "line_items")
    val lineItems: List<LineItem>
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
