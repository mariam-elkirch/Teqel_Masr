package com.example.teqelmasr.displayEquipmentRent.view

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
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.model.Product
import java.util.*

class DisplayRentEquipmentRecyclerAdapter(val context: Context, private val listener: OnProductClickListener) :
    RecyclerView.Adapter<equipmentRentViewHolder>() , Filterable {

    private var equipmentRentList = mutableListOf<Product>()
    private var filteredEquipmentRentList: List<Product> = arrayListOf()
    fun setEquipmentRentList(equipmentRentList: List<Product>) {
        this.equipmentRentList = equipmentRentList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): equipmentRentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SparePartItemBinding.inflate(inflater, parent, false)
        return equipmentRentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: equipmentRentViewHolder, position: Int) {
        Log.i("TAG", "list size ${equipmentRentList.size}" )
        val equipmentRentItem = equipmentRentList[position]
        holder.binding.apply {
            itemTitle.text = equipmentRentItem.title ?: "Unknown"
           itemPrice.text = equipmentRentItem.variants?.get(0)?.price.toString()
           itemCard.setOnClickListener {
           //    val action = DisplayEquipmentRentFragmentDirections.actionDisplayEquipmentRentFragmentToDetailsEquipmentRentFragment(equipmentRentItem)
                listener.onProductClick(equipmentRentItem) }
        }
        Glide.with(context).load(equipmentRentItem.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(holder.binding.itemImage)

    }

    override fun getItemCount() = equipmentRentList.size
    override fun getFilter(): Filter {
        val filter = object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (query == null || query.isEmpty()) {
                    filterResults.values = filteredEquipmentRentList
                } else {
                    val searchKey = query.toString().lowercase(Locale.getDefault())
                    val filteredList = ArrayList<Product>()
                    for (product in filteredEquipmentRentList) {
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
                equipmentRentList = (results!!.values as List<Product>).toMutableList()
                notifyDataSetChanged()
            }
        }
        return filter
    }
}

class equipmentRentViewHolder(val binding: SparePartItemBinding) :
    RecyclerView.ViewHolder(binding.root)