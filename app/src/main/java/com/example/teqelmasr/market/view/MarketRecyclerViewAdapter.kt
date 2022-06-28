package com.example.teqelmasr.market.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.MarketItemBinding
import com.example.teqelmasr.databinding.SparePartItemBinding
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.displaySparePart.view.SparePartsViewHolder
import com.example.teqelmasr.model.Product
import java.util.*


class MarketRecyclerViewAdapter(
    val context: Context,
    private val listener: OnProductClickListener
) :
    RecyclerView.Adapter<MarketViewHolder>() , Filterable {

    private var allProductsList: List<Product> = arrayListOf()
    private var originalAllProductsList: List<Product> = arrayListOf()

    fun setData(allProductsList: List<Product>) {
        this.allProductsList = allProductsList
        this.originalAllProductsList = allProductsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MarketItemBinding.inflate(inflater, parent, false)
        return MarketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val currentProduct = allProductsList[position]
        holder.binding.itemTitle.text = currentProduct.title
        holder.binding.itemPrice.text = "${currentProduct.variants!![0]?.price.toString()} LE"
        holder.binding.itemCard.setOnClickListener { listener.onProductClick(currentProduct) }
        Glide.with(context).load(currentProduct.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(holder.binding.itemImage)
    }

    override fun getItemCount() = allProductsList.size

    override fun getFilter(): Filter {
        val filter = object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (query == null || query.isEmpty()) {
                    filterResults.values = originalAllProductsList
                    Handler(Looper.getMainLooper()).post {
                        listener.onFullList()
                    }
                } else {
                    val searchKey = query.toString().lowercase(Locale.getDefault())
                    val filteredList = ArrayList<Product>()
                    for (product in originalAllProductsList) {
                        if (product.title!!.lowercase(Locale.getDefault()).contains(searchKey)) {
                            filteredList.add(product)
                        }
                    }
                    if (filteredList.count() == 0){
                        Log.i("TAG", "list is empty")
                        Handler(Looper.getMainLooper()).post {
                            //code that runs in main
                            listener.onEmptyList(searchKey)
                        }

                    }else{
                        Handler(Looper.getMainLooper()).post {
                            listener.onFullList()
                        }
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }
            override fun publishResults(query: CharSequence?, results: FilterResults?) {
                allProductsList = results!!.values as List<Product>
                notifyDataSetChanged()
            }
        }
        return filter
    }
}

class MarketViewHolder(val binding: MarketItemBinding) :
    RecyclerView.ViewHolder(binding.root)