package com.example.teqelmasr.editSellerProduct.view

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.addEquipmentSell.view.AddEquipmentSellFragmentDirections
import com.example.teqelmasr.databinding.FragmentEditSellerProductBinding
import com.example.teqelmasr.editSellerProduct.viewModel.EditProductViewModel
import com.example.teqelmasr.editSellerProduct.viewModel.EditProductViewModelFactory
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.model.*
import com.example.teqelmasr.network.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class EditSellerProductFragment : Fragment() {

    private val binding by lazy { FragmentEditSellerProductBinding.inflate(layoutInflater) }
    private val args by navArgs<EditSellerProductFragmentArgs>()
    private val IMAGE_REQ_CODE = 150
    private val TAG = "EditSellerProductFragment"
    private lateinit var typeAdapter: ArrayAdapter<String>
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var imageUri: Uri
    private var equipmentArray: Array<String>? = null
    private var spareArray: Array<String>? = null
    private var categoryArray: Array<String>? = null


    private val factory by lazy {
        EditProductViewModelFactory(
            Repository.getInstance(
                Client.getInstance(),
                requireContext()
            )
        )
    }
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory
        )[EditProductViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        Log.i(TAG, "ADDRESS: ${args.currentProduct.variants?.get(0)?.option1}")
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)


        equipmentArray = context?.resources?.getStringArray(R.array.equiments)
        spareArray = context?.resources?.getStringArray(R.array.spare)
        categoryArray = context?.resources?.getStringArray(R.array.type)

        setUpUI()
        setUpTypeSpinner()
        setUpRadioGroup()

        return binding.root
    }

    private fun setUpRadioGroup() {
        binding.offerGroup.apply {
            args.currentProduct.tags.let {
                when(it){
                    "equimentsell" -> binding.sellBtn.isChecked = true
                    "equimentrent" -> binding.rentBtn.isChecked = true
                    else -> {
                        this.visibility = View.GONE
                        binding.apply {
                            offer.visibility = View.GONE
                            view.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setUpTypeSpinner() {
        args.currentProduct.tags.let {
            binding.typeSpinner.apply {
                when(it){
                    "spare" -> {
                        adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,spareArray!!)
                        setSelection(
                            when(args.currentProduct.productType){
                                getString(R.string.turbocharger) -> 0
                                getString(R.string.filter) -> 1
                                getString(R.string.valve) -> 2
                                getString(R.string.hose) -> 3
                                getString(R.string.miscellaneous) -> 4
                                getString(R.string.hydraulic_components) -> 5
                                else -> {6}
                            }
                        )
                    }else -> {
                        adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,equipmentArray!!)
                        setSelection(
                            when(args.currentProduct.productType){
                                getString(R.string.coldplaners) -> 0
                                getString(R.string.compactors) -> 1
                                getString(R.string.excavators) -> 2
                                getString(R.string.dozers) -> 3
                                else -> {4}
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setUpUI() {
        binding.apply {
            dateTxt.text = args.currentProduct.published_at?.slice(IntRange(0, 9))
            vendorTxt.setText(args.currentProduct.templateSuffix)
            productDesc.setText(args.currentProduct.bodyHtml)
            titleTxt.setText(args.currentProduct.title)
            priceTxt.setText(args.currentProduct.variants?.get(0)?.price.toString())
            addressEdt.text = args.currentProduct.variants?.get(0)?.option1
            categoryTxt.text = args.currentProduct.tags.let {
                when (it) {
                    "spare" -> getString(R.string.spare_parts)
                    "equimentrent" -> getString(R.string.equipment_rent)
                    else -> {
                        getString(R.string.equipment_sell)
                    }
                }
            }
            Glide.with(requireContext()).load(args.currentProduct.image?.src).centerCrop()
                .placeholder(
                    R.drawable.placeholder
                )
                .into(binding.imageItem)

            imageItem.setOnClickListener {
                pickImageFromGallery()
            }

            editLocation.setOnClickListener {

                val iv: ImageView = binding.imageItem as ImageView
                val bitmap = iv.getDrawable().toBitmap()
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
                val bb = bos.toByteArray()
                var imageString: String = Base64.encodeToString(bb, Base64.DEFAULT)
                val imagelist = listOf(ImagesItem(attachment = imageString, filename = "3.png"))
                val img = Image(attachment = imageString, filename = "3.png")
                val variant = Variant(
                    price = binding.priceTxt.text.trim().toString()
                        .toDouble(),
                    id = args.currentProduct.variants?.get(0)?.id,
                    product_id = args.currentProduct.variants?.get(0)?.product_id,
                    option1 = args.currentProduct.variants?.get(0)?.option1
                )
                val optionsItem =
                    OptionsItem(product_id = args.currentProduct.options?.get(0)?.product_id)
                val optionsItems: List<OptionsItem> = listOf(optionsItem)
                val variants: List<Variant> = listOf(variant)
                val product = Product(
                    title = binding.titleTxt.text.trim().toString(),
                    bodyHtml = binding.productDesc.text.trim().toString(),
                    variants = variants,
                    tags = when(binding.sellBtn.isChecked){
                              true -> "equimentsell"
                        else -> {"equimentrent"}
                    },
                    productType = binding.typeSpinner.selectedItem.toString(),
                    images = imagelist,
                    image = img,
                    templateSuffix = binding.vendorTxt.text.trim().toString(),
                    options = optionsItems

                )
                val action =
                    EditSellerProductFragmentDirections.actionEditSellerProductFragmentToMapsFragment(
                        product,
                        null,
                        Constants.EDIT_SOURCE
                    )
                binding.root.findNavController().navigate(action)
            }

            saveFloating.setOnClickListener {

                if (!checkChanges()) {
                    updateProductObject()
                    displayDialog()

                } else {
                    Toast.makeText(context, R.string.no_changes, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkChanges(): Boolean =
        (args.currentProduct.title.equals(binding.titleTxt.text.trim().toString())
                && args.currentProduct.variants?.get(0)?.price.toString() == binding.priceTxt.text.trim()
            .toString()
                && args.currentProduct.bodyHtml.equals(binding.productDesc.text.trim().toString())
                && checkTag()
                && args.currentProduct.productType!! == binding.typeSpinner.selectedItem
                && args.currentProduct.templateSuffix.equals(
            binding.vendorTxt.text.trim().toString()
        ))


    private fun updateProductObject() {
        val iv: ImageView = binding.imageItem as ImageView
        val bitmap = iv.getDrawable().toBitmap()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bb = bos.toByteArray()
        var imageString: String = Base64.encodeToString(bb, Base64.DEFAULT)
        val imagelist = listOf(ImagesItem(attachment = imageString, filename = "3.png"))
        val img = Image(attachment = imageString, filename = "3.png")
        val variant = Variant(
            price = binding.priceTxt.text.trim().toString()
                .toDouble(),
            id = args.currentProduct.variants?.get(0)?.id,
            product_id = args.currentProduct.variants?.get(0)?.product_id,
            option1 = args.currentProduct.variants?.get(0)?.option1
        )
        val optionsItem = OptionsItem(product_id = args.currentProduct.options?.get(0)?.product_id)
        val optionsItems: List<OptionsItem> = listOf(optionsItem)
        val variants: List<Variant> = listOf(variant)
        val product = Product(
            title = binding.titleTxt.text.trim().toString(),
            bodyHtml = binding.productDesc.text.trim().toString(),
            variants = variants,

            tags = when(args.currentProduct.tags){
                        "spare" -> "spare"
                else -> {
                    when(binding.sellBtn.isChecked){
                        true -> "equimentsell"
                        else -> {"equimentsell"}
                    }
                }
            },
            productType = binding.typeSpinner.selectedItem.toString(),
            images = imagelist,
            image = img,
            templateSuffix = binding.vendorTxt.text.trim().toString(),
            options = optionsItems

        )
        val productPost = ProductPost(product)
        //update product
        viewModel.updateProduct(productPost)
    }
    private fun checkTag(): Boolean =
        when(args.currentProduct.tags){
            "spare" -> true
            else -> {
                (args.currentProduct.tags.equals("equimentsell") && binding.sellBtn.isChecked
                        || args.currentProduct.tags.equals("equimentrent") && binding.rentBtn.isChecked)
            }
        }






    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQ_CODE)
    }

    private fun displayDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_progress)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(2500)
            dialog.dismiss()
            Toast.makeText(context, R.string.item_updated, Toast.LENGTH_SHORT)
                .show()
            val action: NavDirections = EditSellerProductFragmentDirections.actionEditSellerProductFragmentToDisplaySellerProductsFragment()
            binding.root.findNavController().navigate(action)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data!!
            binding.imageItem.setImageURI(data.data)
        }

    }
}


