package com.example.teqelmasr.favourite.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDetailsFavouriteBinding
import com.example.teqelmasr.displayEquipmentRent.view.DisplayEquipmentRentFragmentArgs
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModel
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModelFactory
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client

class DetailsFavouriteFragment : Fragment() {
    private val args by navArgs<DetailsFavouriteFragmentArgs>()
private val binding by lazy {FragmentDetailsFavouriteBinding.inflate(layoutInflater)}
    private var isFavorite : Boolean = true
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.getProductDetails(args.productID)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.apply {
            Log.i("TAG", "onCreateView: ${args.productID}")
            viewModel.productDetailsLiveData.observe(requireActivity()) {
            Glide.with(activity?.baseContext!!).load(it.product.images?.get(0)?.src).centerCrop()
                .placeholder(R.drawable.placeholder).into(imageItem)
                titleTxt.text = it.product.title
                priceTxt.text = it.product.variants?.get(0)?.price.toString()
                val dateInString = it.product.updated_at.toString()
                if (dateInString != null) {
                    val dateAfterCut = dateInString.substringBefore('T')
                    dateTxt.text = "${dateAfterCut}"
                }
                categoryTxt.text = context?.resources?.getString(R.string.EquipmentRent)
                typeTxt.text = it.product.productType
                vendorTxt.text = it.product.templateSuffix
                productDesc.text = it.product.bodyHtml
                if (isFavorite){
                    favIcon?.visibility = View.GONE
                    favFillIcon?.visibility = View.VISIBLE
                }else{
                    favFillIcon?.visibility = View.GONE
                    favIcon?.visibility = View.VISIBLE
                }
                favFillIcon.setOnClickListener {

                }
        }
        }
        return binding.root
    }
}