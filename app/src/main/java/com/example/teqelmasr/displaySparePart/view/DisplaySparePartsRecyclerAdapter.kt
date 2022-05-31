package com.example.teqelmasr.displaySparePart.view


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teqelmasr.databinding.SparePartItemBinding


import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem

class DisplaySparePartsRecyclerAdapter : RecyclerView.Adapter<SparePartsViewHolder>() {

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
        val sparePartItem = sparePartsList[position]
        holder.binding.textView.text = sparePartItem.title ?: "Unknown"
        /*Glide.with(context).load(sparePartItem.).placeholder(R.drawable.photo)
            .into(holder.binding.imageView)*/
    }

    override fun getItemCount() = sparePartsList.size
}

class SparePartsViewHolder(val binding: SparePartItemBinding) : RecyclerView.ViewHolder(binding.root)