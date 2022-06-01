package com.example.teqelmasr.displaySellerProducts.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.MyProductsListItemBinding
import com.example.teqelmasr.model.Product

class MyProductsAdapter(private val context: Context, private val onBtnListener: OnBtnListener): RecyclerView.Adapter<MyProductsAdapter.ViewHolder>() {

    private var productList: List<Product> = listOf()
    private val TAG = "MyProducts Adapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(MyProductsListItemBinding.inflate(LayoutInflater.from(parent.context)))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = productList[position]
        holder.binding.apply {
            itemTitle.text = currentItem.title
            price.text = "${currentItem.variants[0].price.toString()} LE"

            deleteBtn.setOnClickListener {
                onBtnListener.onDeleteClick(currentItem)
            }

        }
        Glide.with(context).load(currentItem.image?.src).centerCrop().placeholder(R.drawable.placeholder).into(holder.binding.itemImage)
    }

    override fun getItemCount(): Int = productList.size

    fun setData(products: List<Product>){
        Log.i("TAG", "setData: ${products.size}")
        productList = products
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: MyProductsListItemBinding): RecyclerView.ViewHolder(binding.root)

}