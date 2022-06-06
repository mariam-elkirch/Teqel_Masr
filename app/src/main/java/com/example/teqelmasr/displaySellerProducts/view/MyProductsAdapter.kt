package com.example.teqelmasr.displaySellerProducts.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.MyProductsListItemBinding
import com.example.teqelmasr.model.Product
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class MyProductsAdapter(private val context: Context, private val onBtnListener: OnBtnListener) :
    RecyclerView.Adapter<MyProductsAdapter.ViewHolder>(), Serializable, Filterable {

    private var productList: ArrayList<Product> = ArrayList()
    private var originalList: ArrayList<Product> = ArrayList()


    private val TAG = "MyProductsAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(MyProductsListItemBinding.inflate(LayoutInflater.from(parent.context)))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = productList[position]
        holder.binding.apply {
            itemTitle.text = currentItem.title
            price.text = "${currentItem.variants!![0]?.price.toString()} LE"

            deleteBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage(R.string.delete_message)
                    .setPositiveButton(
                        R.string.yes
                    ) { dialog, _ ->
                        onBtnListener.onDeleteClick(currentItem)
                        productList.removeAt(position)
                        notifyDataSetChanged()
                        dialog.dismiss()
                        Toast.makeText(context, R.string.item_deleted, Toast.LENGTH_SHORT).show()

                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create().show()
            }

            details.setOnClickListener {
                onBtnListener.onDetailsClick(currentItem)
            }

            editBtn.setOnClickListener {
                onBtnListener.onEditClick(currentItem)
            }

        }

        Glide.with(context).load(currentItem.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(holder.binding.itemImage)
    }

    override fun getItemCount(): Int = productList.size

    fun setData(products: ArrayList<Product>) {
        productList = products
        originalList = products
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: MyProductsListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter {
        val filter = object :Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if(p0.isNullOrEmpty()){
                    filterResults.values = originalList

                }else{
                    val searchKey = p0.toString().lowercase(Locale.getDefault())
                    val filteredList = java.util.ArrayList<Product>()

                    for(product in productList){
                        if(product.title!!.lowercase(Locale.getDefault()).contains(searchKey)){
                            filteredList.add(product)
                        }
                    }
                    filterResults.values = filteredList

                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                productList = p1?.values as ArrayList<Product>
                notifyDataSetChanged()
            }

        }
        return filter
    }

}