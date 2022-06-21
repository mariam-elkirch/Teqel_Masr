package com.example.teqelmasr.favourite.view

import FavouriteProduct
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentFavouriteBinding
import com.example.teqelmasr.favourite.FavouriteRecyclerAdapter
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModel
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModelFactory
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client


class FavouriteFragment : Fragment(),OnFavoriteClickListener {
    private val allFavProductList = ArrayList<FavouriteProduct>()
    private val sharedPrefFile = "favorite"
    private  var favProduct : FavouriteProduct? = null
    private var sharedPreferences: SharedPreferences? = null
    private var sharedProductIDs = mutableSetOf<String>()
    private val binding by lazy { FragmentFavouriteBinding.inflate(layoutInflater) }
    private val favouriteAdapter by lazy {
        FavouriteRecyclerAdapter(
            requireContext(),this)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory = AddToFavoriteViewModelFactory(
                Repository.getInstance(
                    Client.getInstance(),
                    requireContext()
                )
            )
        )[AddToFavoriteViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()


        fetchAllFavProducts()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences =
            requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
     binding.apply {
         recyclerViewFav.adapter = favouriteAdapter
         recyclerViewFav.hasFixedSize()
         recyclerViewFav.layoutManager = LinearLayoutManager(requireContext())
     }
        fetchAllFavProducts()
getSavedFavorite()
        return binding.root
    }

    private fun fetchAllFavProducts(){
        viewModel.getFavProduct()
        viewModel.favListLiveData.observe(viewLifecycleOwner){
            Log.i("TAG", "fetchAllFavProducts: ${viewModel.favListLiveData.value?.size}")
            if (viewModel.favListLiveData.value.isNullOrEmpty()){
                favouriteAdapter.setFavouriteList(it)
                favouriteAdapter.notifyDataSetChanged()
                binding.nofav.visibility = View.VISIBLE

            }else {
                favouriteAdapter.setFavouriteList(it)
                favouriteAdapter.notifyDataSetChanged()
                binding.nofav.visibility = View.GONE
            }
        }
    }

    override fun onFavoriteClick(product: FavouriteProduct,listSize : Int) {
        viewModel.deleteFavProduct(product?.draftOrder?.id!!)
        removeFromShared(product?.draftOrder!!.lineItems[0].productID.toString() )
        if (listSize == 1){
            binding.nofav.visibility = View.VISIBLE
        }
    }

    private fun getSavedFavorite() {
        sharedProductIDs = sharedPreferences!!.getStringSet("favID", mutableSetOf())!!
        if (sharedProductIDs.size == 0) {
            viewModel.favListLiveData.observe(requireActivity()) {
                for (fav in it) {
                    favProduct = FavouriteProduct(fav)
                    saveFavorite(favProduct ?: null)
                    getSavedFavorite()
                }
            }
        }
    }
    private fun saveFavorite(product : FavouriteProduct?){
        var editor:SharedPreferences.Editor = sharedPreferences!!.edit()
        sharedProductIDs.add(product?.draftOrder!!.lineItems[0].productID.toString() )
        editor.clear()
        editor.putStringSet("favID",sharedProductIDs)
        editor.apply()
        editor.commit()
    }
    private fun removeFromShared(id : String) {
        sharedProductIDs.remove(id)
        sharedPreferences?.edit()?.clear()?.commit()
        sharedPreferences?.edit()?.putStringSet("favID",sharedProductIDs)?.commit()

    }
}