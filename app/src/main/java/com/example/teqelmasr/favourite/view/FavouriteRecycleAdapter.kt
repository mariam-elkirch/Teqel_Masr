package com.example.teqelmasr.favourite

import DraftOrder
import FavouriteProduct
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FavoriteProductItemBinding
import com.example.teqelmasr.databinding.SparePartItemBinding
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.favourite.view.FavouriteFragment
import com.example.teqelmasr.favourite.view.FavouriteFragmentDirections
import com.example.teqelmasr.favourite.view.OnFavoriteClickListener
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModel
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModelFactory
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class FavouriteRecyclerAdapter(val context: Context,val onClick :OnFavoriteClickListener ) :
    RecyclerView.Adapter<FavouriteViewHolder>()  {

    private var favouriteList = mutableListOf<DraftOrder>()
    private var originalfavouriteList: List<DraftOrder> = arrayListOf()
    fun setFavouriteList(favouriteList: List<DraftOrder>) {
        this.favouriteList = favouriteList.toMutableList()
        this.originalfavouriteList = favouriteList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavoriteProductItemBinding.inflate(inflater, parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        Log.i("TAG", "list size ${favouriteList.size}" )
        val favouriteItem = favouriteList[position]
        holder.binding.apply {
            itemTitle.text = favouriteItem.lineItems.get(0).title ?: "Unknown"
            itemPrice.text = "${favouriteItem.lineItems.get(0).price} LE"
            itemCard.setOnClickListener {
                    val action =
                        FavouriteFragmentDirections.actionFavouriteFragmentToDetailsFavouriteFragment(
                            favouriteItem.lineItems.get(0).productID,favouriteItem?.id!!)
                    it.findNavController().navigate(action)
            }
            deleteFavButton.setOnClickListener { onClick.onFavoriteClick(FavouriteProduct(favouriteItem)) }
        }
        if (favouriteItem.noteAttributes.isNotEmpty()) {
            Glide.with(context).load(favouriteItem.noteAttributes.get(0).value).centerCrop()
                .placeholder(R.drawable.placeholder).into(holder.binding.itemImage)
        }else{
            holder.binding.itemImage.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount() = favouriteList.size

//    override fun getFilter(): Filter {
//        val filter = object : Filter() {
//            override fun performFiltering(query: CharSequence?): FilterResults {
//                val filterResults = FilterResults()
//                if (query == null || query.isEmpty()) {
//                    filterResults.values = originalfavouriteList
//                    Handler(Looper.getMainLooper()).post {
//                        listener.onFullList()
//                    }
//                } else {
//                    val searchKey = query.toString().lowercase(Locale.getDefault())
//                    val filteredList = ArrayList<FavouriteProduct>()
//                    for (product in originalfavouriteList) {
//                        if (product.draftOrder.lineItems.get(0).title!!.lowercase(Locale.getDefault()).contains(searchKey)) {
//                            filteredList.add(product)
//                        }
//                    }
//                    if (filteredList.count() == 0){
//                        Log.i("TAG", "list is empty")
//                        Handler(Looper.getMainLooper()).post {
//                            //code that runs in main
//                            listener.onEmptyList(searchKey)
//                        }
//
//                    }else{
//                        Handler(Looper.getMainLooper()).post {
//                            listener.onFullList()
//                        }
//                    }
//                    filterResults.values = filteredList
//                }
//                return filterResults
//            }
//            override fun publishResults(query: CharSequence?, results: FilterResults?) {
//                favouriteList = (results!!.values as List<FavouriteProduct>).toMutableList()
//                notifyDataSetChanged()
//            }
//        }
//        return filter
//    }
}

class FavouriteViewHolder(val binding: FavoriteProductItemBinding) :
    RecyclerView.ViewHolder(binding.root)