package com.example.teqelmasr.addEquipmentSell.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.addEquipmentSell.viewmodel.AddProductViewModel
import com.example.teqelmasr.addEquipmentSell.viewmodel.AddProductViewModelFactory

import com.example.teqelmasr.databinding.FragmentAddEquipmentSellBinding
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductPost
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap.CompressFormat

import android.graphics.drawable.BitmapDrawable

import android.R
import android.view.View.*
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.teqelmasr.model.Variant


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEquipmentSellFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEquipmentSellFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAddEquipmentSellBinding
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var viewModel: AddProductViewModel
    lateinit var mytag : String
    lateinit var addProductfactory:AddProductViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddEquipmentSellBinding.inflate(inflater, container, false)
        binding.productImg.setOnClickListener {

            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        val spinner = binding.spinner
        val text = spinner.selectedItem.toString()
       /* if(text.equals("Equipment for Sell")){
            binding.spinnerSpare.visibility = GONE
            binding.spinnerEquipment.visibility = VISIBLE
        }
        else{
            binding.spinnerEquipment.visibility = GONE
            binding.spinnerSpare.visibility = VISIBLE
        }*/
      spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
               // binding.spinnerSpare.visibility = GONE
               // binding.spinnerEquipment.visibility = VISIBLE
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("tag",position.toString()+"Imggg")
                if (position == 2){
                binding.spinnerEquipment.visibility = INVISIBLE
                binding.spinnerSpare.visibility = VISIBLE
                     mytag = "spare"
            }else{
                    binding.spinnerEquipment.visibility = VISIBLE
                    binding.spinnerSpare.visibility = INVISIBLE
                    if(position == 0)
                        mytag = "equimentsell"
                    else{
                        mytag = "equimentrent"
                    }
            }
            }

        }
        binding.saveButton.setOnClickListener {



            val iv: ImageView = binding.productImg as ImageView
            val bitmap = iv.getDrawable().toBitmap()
            val bos = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.PNG, 100, bos)
            val bb = bos.toByteArray()
            val imageString: String = Base64.encodeToString(bb, Base64.DEFAULT)

            Log.i("Tag", "Imgggggggg"+mytag)
//decode
         /*   val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
           binding.productImg.setImageBitmap(decodedImage)*/
//end decode
            val doublePrice: Double? = binding.priceEditText.text.toString().toDoubleOrNull()
            Log.i("tag",doublePrice.toString()+ "Priceee")
            val varian = listOf(

              Variant(price = doublePrice)
            )

            if(binding.titleEditText.toString().trim().length>0){

            var product = ProductPost(Product(title = binding.titleEditText.text.toString(), tags = mytag
                ,bodyHtml = binding.describtionEditText.text.toString()
                ,templateSuffix = binding.manfactoryEditText.text.toString(), variants = varian))
            Log.i("Tag", "Imgggggggg"+binding.titleEditText.text.toString())

                addProductfactory = AddProductViewModelFactory(
                    Repository.getInstance(
                        Client.getInstance(),
                        requireContext()
                    ))
                // ViewModelProvider(this,addProductfactory).get(AddProductViewModel::class.java)
                viewModel = ViewModelProvider(requireActivity(), addProductfactory)[AddProductViewModel::class.java]
                viewModel.myProducts.observe(viewLifecycleOwner){
                    Log.i("tag",it.toString()+ "product")
                    val toast = Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT)
                    toast.show()
                }
                viewModel.errorMessage.observe(viewLifecycleOwner){
                    Log.i("tag",it.toString()+ "product")
                }
                viewModel.postProduct(product)
            }

        }

        return  binding.root
       // return inflater.inflate(R.layout.fragment_add_equipment_sell, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
                    binding.productImg.setImageURI(imageUri)

        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEquipmentSellFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddEquipmentSellFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}