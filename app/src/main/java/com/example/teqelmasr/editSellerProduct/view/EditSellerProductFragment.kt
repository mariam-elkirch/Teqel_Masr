package com.example.teqelmasr.editSellerProduct.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentEditSellerProductBinding
import com.example.teqelmasr.editSellerProduct.viewModel.EditProductViewModel
import com.example.teqelmasr.editSellerProduct.viewModel.EditProductViewModelFactory
import com.example.teqelmasr.model.*
import com.example.teqelmasr.network.Client
import java.io.ByteArrayOutputStream

class EditSellerProductFragment : Fragment() {

    private val binding by lazy { FragmentEditSellerProductBinding.inflate(layoutInflater) }
    private val args by navArgs<EditSellerProductFragmentArgs>()
    private val IMAGE_REQ_CODE = 150
    private val TAG = "EditSellerProductFragment"
    private lateinit var typeAdapter: ArrayAdapter<String>
    private lateinit var categoryAdapter: ArrayAdapter<String>

    private  var equipmentArray : Array<String>? = null
    private  var spareArray : Array<String>? = null
    private  var categoryArray : Array<String>? = null


    private val factory by lazy { EditProductViewModelFactory(Repository.getInstance(Client.getInstance(),requireContext())) }
    private val viewModel by lazy { ViewModelProvider(requireActivity(),factory)[EditProductViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        equipmentArray = context?.resources?.getStringArray(R.array.equiments)
       spareArray = context?.resources?.getStringArray(R.array.spare)
         categoryArray = context?.resources?.getStringArray(R.array.type)

        setUpUI()

        setUpSpinners()

        return binding.root
    }

    private fun setUpUI() {
        binding.apply {
            dateTxt.text = args.currentProduct.published_at?.slice(IntRange(0, 9))
            vendorTxt.setText(args.currentProduct.templateSuffix)
            productDesc.setText(args.currentProduct.bodyHtml)
            titleTxt.setText(args.currentProduct.title)
            priceTxt.setText(args.currentProduct.variants?.get(0)?.price.toString())

            Glide.with(requireContext()).load(args.currentProduct.image?.src).centerCrop()
                .placeholder(
                    R.drawable.placeholder
                )
                .into(binding.imageItem)

            imageItem.setOnClickListener {
                pickImageFromGallery()
            }

            saveTxt.setOnClickListener {
                if(!checkChanges()){
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(R.string.save_message)
                        .setPositiveButton(
                            R.string.save
                        ) { dialog, _ ->

                            updateProductObject()
                            dialog.dismiss()
                            Toast.makeText(context, R.string.item_updated, Toast.LENGTH_SHORT).show()
                            val action: NavDirections = EditSellerProductFragmentDirections.actionEditSellerProductFragmentToDisplaySellerProductsFragment(null)
                            binding.root.findNavController().navigate(action)

                        }
                        .setNegativeButton(R.string.discard) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create().show()

                }else{
                    Toast.makeText(context, R.string.no_changes, Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

    private fun checkChanges(): Boolean = (args.currentProduct.title.equals(binding.titleTxt.text.trim().toString())
            && args.currentProduct.variants?.get(0)?.price.toString() == binding.priceTxt.text.trim().toString()
            && args.currentProduct.bodyHtml.equals(binding.productDesc.text.trim().toString())
            && args.currentProduct.tags?.equals(binding.categorySpinner.selectedItem.toString())!!
            && args.currentProduct.productType!! == binding.typeSpinner.selectedItem
            && args.currentProduct.templateSuffix.equals(binding.vendorTxt.text.trim().toString()))


    private fun updateProductObject() {
        val iv: ImageView = binding.imageItem as ImageView
        val bitmap = iv.getDrawable().toBitmap()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bb = bos.toByteArray()
        var imageString: String = Base64.encodeToString(bb, Base64.DEFAULT)
        val imagelist = listOf(ImagesItem(attachment = imageString, filename = "3.png") )
        val img = Image(attachment = imageString, filename = "3.png")
        val variant = Variant(
            price = binding.priceTxt.text.trim().toString()
                .toDouble(),
            id = args.currentProduct.variants?.get(0)?.id,
            product_id = args.currentProduct.variants?.get(0)?.product_id
        )
        val optionsItem = OptionsItem(product_id = args.currentProduct.options?.get(0)?.product_id)
        val optionsItems: List<OptionsItem> = listOf(optionsItem)
        val variants: List<Variant> = listOf(variant)
        val product = Product(
            title = binding.titleTxt.text.trim().toString(),
            bodyHtml = binding.productDesc.text.trim().toString(),
            variants = variants,
            tags = binding.categorySpinner.selectedItem.toString(),
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

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQ_CODE)
    }


    private fun setUpSpinners() {
        categoryAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryArray!!
        )
        typeAdapter = when (args.currentProduct.tags) {
            R.string.spare_tag.toString() -> ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item,spareArray!!)
            else -> {
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, equipmentArray!!)
            }
        }
        binding.apply {
            categorySpinner.adapter = categoryAdapter
            categorySpinner.setSelection(
                when (args.currentProduct.tags) {
                    R.string.sell_equip_tag.toString() -> 0
                    R.string.rent_equip_tag.toString() -> 1
                    else -> { 2 }
                }
            )

/*            if(args.currentProduct.tags.equals("equimentsell") || args.currentProduct.tags.equals("equimentrent")){
                typeSpinner.setSelection(when(args.currentProduct.productType){
                    "coldplaners" -> 0
                    "compactors" -> 1
                    "excavators" -> 2
                    "dozers" -> 3
                    "asphaltpavers" -> 4
                    "backhoeloaders" -> 5
                    "articulatedtrucks" -> 6
                    else -> {7}
                })
            }else{
                typeSpinner.setSelection(when(args.currentProduct.productType){
                    "turbocharger" -> 0
                    "filter" -> 1
                    "accumulator" -> 2
                    "valve" -> 3
                    "hose" -> 4
                    "miscellaneous" -> 5
                    "hydraulic_components" -> 6
                    else -> {7}
                })
            }*/
           categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
               override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                   Log.i(TAG, "onItemSelected: ${p0?.selectedItem.toString()}")
                   typeAdapter = when (p0?.selectedItem.toString()) {
                       R.string.sell_equip_tag.toString() -> ArrayAdapter<String>(
                           requireContext(),
                           android.R.layout.simple_spinner_item,
                           equipmentArray!!
                       )
                       R.string.rent_equip_tag.toString() -> ArrayAdapter<String>(
                           requireContext(),
                           android.R.layout.simple_spinner_item,
                           equipmentArray!!
                       )
                       R.string.spare_tag.toString() -> ArrayAdapter<String>(
                           requireContext(),
                           android.R.layout.simple_spinner_item,
                           spareArray!!
                       )
                       else -> {
                           ArrayAdapter<String>(
                               requireContext(),
                               android.R.layout.simple_spinner_item,
                               equipmentArray!!
                           )
                       }
                   }

                   typeSpinner.adapter = typeAdapter


               }

               override fun onNothingSelected(p0: AdapterView<*>?) {
                   typeSpinner.setSelection(when(args.currentProduct.productType){
                       R.string.turbocharger.toString() -> 0
                       R.string.filter.toString() -> 1
                       R.string.accumulator.toString() -> 2
                       R.string.valve.toString() -> 3
                       R.string.hose.toString() -> 4
                       R.string.miscellaneous.toString() -> 5
                       R.string.hydraulic_components.toString() -> 6
                       else -> {7}
                   })
               }

           }

        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK) {
            binding.imageItem.setImageURI(data?.data)
        }

    }


}