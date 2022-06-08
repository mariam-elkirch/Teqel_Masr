package com.example.teqelmasr.displaySparePart.view


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
import com.example.teqelmasr.databinding.SparePartItemBinding
import com.example.teqelmasr.model.Product
import java.util.*

class DisplaySparePartsRecyclerAdapter(
    val context: Context,
    private val listener: OnProductClickListener
) :
    RecyclerView.Adapter<SparePartsViewHolder>(), Filterable {

    private var sparePartsList: List<Product> = arrayListOf()
    private var originalSparePartsList: List<Product> = arrayListOf()

    fun setData(sparePartsList: List<Product>) {
        this.sparePartsList = sparePartsList
        this.originalSparePartsList = sparePartsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SparePartsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SparePartItemBinding.inflate(inflater, parent, false)
        return SparePartsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SparePartsViewHolder, position: Int) {
      //  Log.i("TAG", "list size ${sparePartsList.size}")
        val sparePartItem = sparePartsList[position]
        holder.binding.apply {
            itemTitle.text = sparePartItem.title ?: "Unknown"
            itemPrice.text = "${sparePartItem.variants!![0]?.price.toString()} LE"
            itemCard.setOnClickListener { listener.onProductClick(sparePartItem) }
        }
        Glide.with(context).load(sparePartItem.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(holder.binding.itemImage)

    }

    override fun getItemCount() = sparePartsList.size

    override fun getFilter(): Filter {
        val filter = object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (query == null || query.isEmpty()) {
                    filterResults.values = originalSparePartsList
                    Handler(Looper.getMainLooper()).post {
                        listener.onFullList()
                    }
                } else {
                    val searchKey = query.toString().lowercase(Locale.getDefault())
                    val filteredList = ArrayList<Product>()
                    for (product in originalSparePartsList) {
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
                sparePartsList = results!!.values as List<Product>
                notifyDataSetChanged()
            }
        }
        return filter
    }
}

class SparePartsViewHolder(val binding: SparePartItemBinding) :
    RecyclerView.ViewHolder(binding.root)

