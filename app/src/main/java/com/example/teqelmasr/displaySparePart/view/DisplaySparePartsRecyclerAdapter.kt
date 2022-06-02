package com.example.teqelmasr.displaySparePart.view


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.SparePartItemBinding
import com.example.teqelmasr.model.Product

class DisplaySparePartsRecyclerAdapter(val context: Context, private val listener: OnProductClickListener) :
    RecyclerView.Adapter<SparePartsViewHolder>() {

    private var sparePartsList = mutableListOf<Product>()

    fun setSparePartsList(sparePartsList: List<Product>) {
        this.sparePartsList = sparePartsList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SparePartsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SparePartItemBinding.inflate(inflater, parent, false)
        return SparePartsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SparePartsViewHolder, position: Int) {
        Log.i("TAG", "list size ${sparePartsList.size}" )
        val sparePartItem = sparePartsList[position]
        holder.binding.apply {
            itemTitle.text = sparePartItem.title ?: "Unknown"
            itemPrice.text = sparePartItem.variants?.get(0)?.price.toString()
            itemCard.setOnClickListener { listener.onProductClick(sparePartItem) }
        }
        Glide.with(context).load(sparePartItem.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(holder.binding.itemImage)

    }

    override fun getItemCount() = sparePartsList.size
}

class SparePartsViewHolder(val binding: SparePartItemBinding) :
    RecyclerView.ViewHolder(binding.root)