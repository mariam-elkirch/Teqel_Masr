package com.example.teqelmasr.model
import FavProducts
import FavouriteProduct
import com.example.teqelmasr.network.RemoteSource
import retrofit2.Response

class FakeDataSource: RemoteSource {

    private var productItemRes: Response<ProductItem>? = null
    private val customersResponse: Response<CustomersResponse>? = null
    private val customerItemRes: Response<CustomerItem>? = null
    private val favouriteProductRes: Response<FavouriteProduct>? = null
    private val favouriteProductsRes: Response<FavProducts>? = null
    private val oneProductRes: Response<OneProduct>? = null


    private val product:Product = Product(title = "test")
    private val products: ArrayList<Product> = arrayListOf(product)
    private val productItem = ProductItem(products = products)

    override suspend fun storeProduct(product: ProductPost): Response<ProductItem> {
        return productItemRes!!
    }

    override suspend fun getProductsByCategory(productCategory: Long): Response<ProductItem> {
        return productItemRes!!

    }

    //TEST
    override suspend fun getMyProducts(): ProductItem {
        return productItem

    }

    override suspend fun getAllProducts(): Response<ProductItem> {
        return productItemRes!!

    }

    override suspend fun deleteProduct(product: Product) {
    }

    override suspend fun updateProduct(product: ProductPost) {
    }

    override suspend fun postCustomer(customer: Customer) {
    }

    override suspend fun updateCustomer(customer: Customer) {
    }

    override suspend fun getCustomers(): Response<CustomersResponse> {
        return customersResponse!!
    }

    override suspend fun getCustomer(): Response<CustomerItem> {
        return customerItemRes!!
    }

    override suspend fun addToFavorite(product: FavouriteProduct): Response<FavouriteProduct> {
        return favouriteProductRes!!
    }

    override suspend fun deleteFavProduct(id: Long) {
    }

    override suspend fun getFavProducts(): Response<FavProducts> {
        return favouriteProductsRes!!
    }

    override suspend fun getSpecificProduct(id: Long): Response<OneProduct> {
        return oneProductRes!!
    }
}