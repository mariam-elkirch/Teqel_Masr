package com.example.teqelmasr.favourite.view

import FavouriteProduct
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.databinding.FragmentFavouriteBinding
import com.example.teqelmasr.favourite.FavouriteRecyclerAdapter
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModel
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModelFactory
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client


class FavouriteFragment : Fragment() {
    private val allFavProductList = ArrayList<FavouriteProduct>()
    private val binding by lazy { FragmentFavouriteBinding.inflate(layoutInflater) }
    private val favouriteAdapter by lazy {
        FavouriteRecyclerAdapter(
            requireContext())
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     binding.apply {
         recyclerViewFav.adapter = favouriteAdapter
         recyclerViewFav.hasFixedSize()
         recyclerViewFav.layoutManager = LinearLayoutManager(requireContext())
     }
        fetchAllFavProducts()
        return binding.root
    }

    fun fetchAllFavProducts(){
        viewModel.getFavProduct()
        viewModel.favListLiveData.observe(viewLifecycleOwner){
            if (viewModel.favListLiveData.value.isNullOrEmpty()){
                binding.nofav.visibility = View.VISIBLE
                favouriteAdapter.setFavouriteList(it)
            }else {
                favouriteAdapter.setFavouriteList(it)
                binding.nofav.visibility = View.GONE
            }
        }
    }

}