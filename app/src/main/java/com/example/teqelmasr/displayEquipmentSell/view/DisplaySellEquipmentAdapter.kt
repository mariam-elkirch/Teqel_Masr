package com.example.teqelmasr.displayEquipmentSell.view

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

class DisplaySellEquipmentAdapter (val context: Context, private val listener: OnProductClickListener) :
    RecyclerView.Adapter<equipmentSellViewHolder>() , Filterable {

    private var equipmentSellList = mutableListOf<Product>()
    private var filteredEquipmentSellList: List<Product> = arrayListOf()
    fun setEquipmentSellList(equipmentSellList: List<Product>) {
        this.equipmentSellList = equipmentSellList.toMutableList()
        this.filteredEquipmentSellList = equipmentSellList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): equipmentSellViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SparePartItemBinding.inflate(inflater, parent, false)
        return equipmentSellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: equipmentSellViewHolder, position: Int) {
        Log.i("TAG", "list size ${equipmentSellList.size}" )
        val equipmentSellItem = equipmentSellList[position]
        holder.binding.apply {
            itemTitle.text = equipmentSellItem.title ?: "Unknown"
            itemPrice.text = equipmentSellItem.variants?.get(0)?.price.toString()
            itemCard.setOnClickListener {
               // val action = DisplayEquipmentRentFragmentDirections.actionDisplayEquipmentRentFragmentToDetailsEquipmentRentFragment(equipmentRentItem)
                listener.onProductClick(equipmentSellItem) }
        }
        Glide.with(context).load(equipmentSellItem.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(holder.binding.itemImage)

    }

    override fun getItemCount() = equipmentSellList.size
    override fun getFilter(): Filter {
        val filter = object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (query == null || query.isEmpty()) {
                    filterResults.values = filteredEquipmentSellList
                } else {
                    val searchKey = query.toString().lowercase(Locale.getDefault())
                    val filteredList = ArrayList<Product>()
                    for (product in filteredEquipmentSellList) {
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
                equipmentSellList = (results!!.values as List<Product>).toMutableList()
                notifyDataSetChanged()
            }
        }
        return filter
    }
}

class equipmentSellViewHolder(val binding: SparePartItemBinding) :
    RecyclerView.ViewHolder(binding.root)