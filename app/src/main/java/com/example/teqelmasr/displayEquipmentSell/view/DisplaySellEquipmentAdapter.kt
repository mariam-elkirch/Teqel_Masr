package com.example.teqelmasr.displayEquipmentSell.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.SparePartItemBinding

import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.model.Product

class DisplaySellEquipmentAdapter (val context: Context, private val listener: OnProductClickListener) :
    RecyclerView.Adapter<equipmentSellViewHolder>() {

    private var equipmentSellList = mutableListOf<Product>()

    fun setEquipmentSellList(equipmentSellList: List<Product>) {
        this.equipmentSellList = equipmentSellList.toMutableList()
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
}

class equipmentSellViewHolder(val binding: SparePartItemBinding) :
    RecyclerView.ViewHolder(binding.root)