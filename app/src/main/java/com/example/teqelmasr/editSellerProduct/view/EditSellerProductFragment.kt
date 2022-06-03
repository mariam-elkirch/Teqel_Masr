package com.example.teqelmasr.editSellerProduct.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentEditSellerProductBinding

class EditSellerProductFragment : Fragment() {

    private val binding by lazy { FragmentEditSellerProductBinding.inflate(layoutInflater) }
    private val args by navArgs<EditSellerProductFragmentArgs>()
    private val IMAGE_REQ_CODE = 150
    private val TAG = "EditSellerProductFragment"
    private lateinit var typeAdapter: ArrayAdapter<String>
    private lateinit var categoryAdapter: ArrayAdapter<String>

    private val equipmentArray = arrayOf("coldplaners", "compactors", "excavators", "dozers", "asphaltpavers", "backhoeloaders","articulatedtrucks", "Other")
    private val spareArray = arrayOf("turbocharger", "filter", "accumulator", "valve", "hose", "miscellaneous","hydraulic_components", "Other")
    private val categoryArray = arrayOf("Equipment For Sell", "Equipment For Rent", "Spare Parts")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        setUpUI()

        setUpSpinners()

        return binding.root
    }

    private fun setUpUI() {
        binding.apply {
            dateTxt.text = args.currentProduct.published_at?.slice(IntRange(0,9))
            vendorTxt.setText(args.currentProduct.templateSuffix)
            productDesc.setText(args.currentProduct.bodyHtml)
            titleTxt.setText(args.currentProduct.title)
            priceTxt.setText(args.currentProduct.variants?.get(0)?.price.toString())

            Glide.with(requireContext()).load(args.currentProduct.image?.src).centerCrop().placeholder(
                R.drawable.placeholder)
               .into(binding.imageItem)

            imageItem.setOnClickListener {
                pickImageFromGallery()
            }

            saveTxt.setOnClickListener {
                TODO()//EDIT
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQ_CODE)
    }


    private fun setUpSpinners() {
        categoryAdapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, categoryArray)
        typeAdapter = when(args.currentProduct.tags){
            "equipment" -> ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, equipmentArray)
            "spare" -> ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, spareArray)
            else -> {ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, equipmentArray)}
        }
        binding.categorySpinner.adapter = categoryAdapter
        binding.typeSpinner.adapter = typeAdapter
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK){
            binding.imageItem.setImageURI(data?.data)
        }

    }


}