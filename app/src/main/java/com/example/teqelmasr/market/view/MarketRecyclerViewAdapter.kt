package com.example.teqelmasr.market.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.MarketItemBinding
import com.example.teqelmasr.databinding.SparePartItemBinding
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.displaySparePart.view.SparePartsViewHolder
import com.example.teqelmasr.model.Product


class MarketRecyclerViewAdapter(
    val context: Context,
    private val listener: OnProductClickListener
) :
    RecyclerView.Adapter<MarketViewHolder>() {

    private var allProductsList: List<Product> = arrayListOf()

    fun setData(allProductsList: List<Product>) {
        this.allProductsList = allProductsList
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
}

class MarketViewHolder(val binding: MarketItemBinding) :
    RecyclerView.ViewHolder(binding.root)