package com.example.teqelmasr.displayEquipmentRent.view

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

class DisplayRentEquipmentRecyclerAdapter(val context: Context, private val listener: OnProductClickListener) :
    RecyclerView.Adapter<equipmentRentViewHolder>() {

    private var equipmentRentList = mutableListOf<Product>()

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
            textView.text = equipmentRentItem.title ?: "Unknown"
            priceTextView.text = equipmentRentItem.variants?.get(0)?.price.toString()
            sparePartCardView.setOnClickListener {
               val action = DisplayEquipmentRentFragmentDirections.actionDisplayEquipmentRentFragmentToDetailsEquipmentRentFragment(equipmentRentItem)
                listener.onProductClick(equipmentRentItem) }
        }
        Glide.with(context).load(equipmentRentItem.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(holder.binding.imageView)

    }

    override fun getItemCount() = equipmentRentList.size
}

class equipmentRentViewHolder(val binding: SparePartItemBinding) :
    RecyclerView.ViewHolder(binding.root)