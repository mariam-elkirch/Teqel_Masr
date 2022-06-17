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
import com.example.teqelmasr.network.Client
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap.CompressFormat


import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.view.View.*
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.teqelmasr.R
import com.example.teqelmasr.displayEquipmentSell.view.DisplayEquipmentSellFragmentDirections
import com.example.teqelmasr.displayEquipmentSell.view.EquimentSellBottonSheetFrgmentDirections
import com.example.teqelmasr.displaySparePart.view.DetailsSparePartFragmentArgs
import com.example.teqelmasr.editSellerProduct.view.EditSellerProductFragmentDirections
import com.example.teqelmasr.model.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
    private val args by navArgs<AddEquipmentSellFragmentArgs>()
    lateinit var viewModel: AddProductViewModel
    var mytag : String = ""
     var myproductType : String =""
    var producttype: String = ""
    var myproductTypeEquipment : String =""
    var locationFromMAp : String =""
    lateinit var productToMap : AddEditProduct
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
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        // Inflate the layout for this fragment
        binding = FragmentAddEquipmentSellBinding.inflate(inflater, container, false)
        binding.productImg.setOnClickListener {

            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }


         putOnEditTextDataFromArgs()
        val spinner = binding.spinner
         val spinnerspare = binding.spinnerSpare
        val spinnerEquipment = binding.spinnerEquipment

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
                     mytag =  "spare"
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

           spinnerEquipment?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Log.i("tag",position.toString()+"Imggg"+mytag)

                        when (position) {
                            0 -> myproductTypeEquipment = "Coldplaners"
                            1 -> {
                                Log.i("tag",position.toString()+"positionnnnnnnn")
                                myproductTypeEquipment = "Compactors"
                            }
                            2 -> {
                                myproductTypeEquipment = "Excavators"
                            }

                            3 -> {
                                myproductTypeEquipment = "Dozers"
                            }
                            else -> {
                                myproductTypeEquipment = "Other"
                            }
                        }
                    }

        }
        spinnerspare?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("tag",position.toString()+"Imggg"+mytag)

                when (position) {
                    0 -> myproductType = "Turbocharger"
                    1 -> {
                        Log.i("tag",position.toString()+"positionnnnnnnn")
                        myproductType = "Filter"
                    }
                    2 -> {
                        myproductType = "Accumulator"
                    }
                    3 -> {
                        myproductType =  "Valve"
                    }
                    4 -> {
                        myproductType = "Hose"
                    }
                    5 -> {
                        myproductType =  "Miscellaneous"
                    }
                    6 -> {
                        myproductType = "Hydraulic Components"
                    }
                    else -> {
                        myproductType = "Other"
                    }
                }
            }

        }

       // Log.i("Tag", "To Map"+binding.titleEditText.getText().toString())

        binding.myLocation.setOnClickListener {

            fillEnteredDataToMap()
            val action = AddEquipmentSellFragmentDirections.actionAddEquipmentSellFragmentToMapsFragment(productToMap)
            binding.root.findNavController().navigate(action)
        }


        addProductfactory = AddProductViewModelFactory(
            Repository.getInstance(
                Client.getInstance(),
                requireContext()
            ))
        // ViewModelProvider(this,addProductfactory).get(AddProductViewModel::class.java)
        viewModel = ViewModelProvider(requireActivity(), addProductfactory)[AddProductViewModel::class.java]
        binding.saveButton.setOnClickListener {
             checkDataEnter()
//decode
         /*   val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
           binding.productImg.setImageBitmap(decodedImage)*/
//end decode
            if(checkData()){
                val builder = AlertDialog.Builder(context)
                builder.setMessage(com.example.teqelmasr.R.string.save)
                    .setPositiveButton(
                        com.example.teqelmasr.R.string.save
                    ) { dialog, _ ->
                          saveProductObject()
                        dialog.dismiss()
                      /*  Toast.makeText(context, com.example.teqelmasr.R.string.save, Toast.LENGTH_SHORT).show()
                         val action: NavDirections = AddEquipmentSellFragmentDirections.actionAddEquipmentSellFragmentToDisplaySellerProductsFragment()
                         binding.root.findNavController().navigate(action)*/
                        displayDialog()

                    }
                    .setNegativeButton(com.example.teqelmasr.R.string.discard) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create().show()



            }
            else{
                Log.i("tag", "")
            }

        }
        viewModel.myProducts.observe(viewLifecycleOwner){
            Log.i("tag","producttttttttt"+it.toString()+ "product")

            // val toast = Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT)
            //toast.show()
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){
            Log.i("tag",it.toString()+ "product")
        }


        return  binding.root
       // return inflater.inflate(R.layout.fragment_add_equipment_sell, container, false)
    }

    private fun  putOnEditTextDataFromArgs() {
        if(!args.productFromMap?.address.isNullOrEmpty() && !args.productFromMap?.address.equals("")){
            locationFromMAp = args.productFromMap?.address!!

        }
        binding.myLocation.setText(locationFromMAp)
        if(args.productFromMap != null) {
            binding.titleEditText.setText(args.productFromMap?.title)
            binding.describtionEditText.setText(args.productFromMap?.describtion)
            binding.manfactoryEditText.setText(args.productFromMap?.manfactory)
            binding.priceEditText.setText(args.productFromMap?.price)
            binding.telphoneEditText.setText(args.productFromMap?.telephone)
            binding.productImg.setImageURI(args.productFromMap?.imageUri)
            if(args.productFromMap?.Type.equals("equimentsell")) {
                binding.spinner.setSelection(0)

                setPositionEquipmentSpinner()

            }
            else if (args.productFromMap?.Type.equals("equimentrent")){
                binding.spinner.setSelection(1)
              setPositionEquipmentSpinner()
            }
            else {
                binding.spinner.setSelection(2)
                setPositionSparePartsSpinner()
            }



        }
    }

    private fun setPositionEquipmentSpinner() {
        if(args.productFromMap?.category.equals("Coldplaners")){
            binding.spinnerEquipment.setSelection(0)
        }
        else if(args.productFromMap?.category.equals("Compactors")){
            binding.spinnerEquipment.setSelection(1)
        }
        else if(args.productFromMap?.category.equals("Excavators")){
            binding.spinnerEquipment.setSelection(2)
        }
        else if(args.productFromMap?.category.equals("Dozers")){
            binding.spinnerEquipment.setSelection(3)
        }
        else{
            binding.spinnerEquipment.setSelection(4)
        }
    }
    private fun setPositionSparePartsSpinner() {
        if(args.productFromMap?.category.equals("Turbocharger")){
            binding.spinnerSpare.setSelection(0)
        }
        else if(args.productFromMap?.category.equals("Filter")){
            binding.spinnerSpare.setSelection(1)
        }
        else if(args.productFromMap?.category.equals("Accumulator")){
            binding.spinnerSpare.setSelection(2)
        }

        else if(args.productFromMap?.category.equals("Valve")){
            binding.spinnerSpare.setSelection(3)
        }
        else if(args.productFromMap?.category.equals("Hose")){
            binding.spinnerSpare.setSelection(4)
        }
        else if(args.productFromMap?.category.equals("Miscellaneous")){
            binding.spinnerSpare.setSelection(5)
        }
        else if(args.productFromMap?.category.equals("Hydraulic Components")){
            binding.spinnerSpare.setSelection(6)
        }
        else{
            binding.spinnerSpare.setSelection(7)
        }
    }
    private fun fillEnteredDataToMap() {
        if(mytag.equals("spare")){
            producttype = myproductType
        }
        else{
            producttype = myproductTypeEquipment
        }
        Log.i("Tag", "To Map"+binding.titleEditText.getText().toString())
        productToMap = AddEditProduct(Type = mytag,category = producttype,
            title = binding.titleEditText.text.toString(),
            manfactory = binding.manfactoryEditText.text.toString(),
            price = binding.priceEditText.text.toString(),telephone = binding.telphoneEditText.text.toString(),
            imageUri = imageUri,describtion = binding.describtionEditText.text.toString())

    }

    private fun saveProductObject() {
        val iv: ImageView = binding.productImg as ImageView
        val bitmap = iv.getDrawable().toBitmap()
        val bos = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 100, bos)
        val bb = bos.toByteArray()
        var imageString: String = Base64.encodeToString(bb, Base64.DEFAULT)

        Log.i("Tag", "Imgggggggg"+imageString)
        Log.i("tag",myproductType+ "Priceee")
        val doublePrice: Double? = binding.priceEditText.text.toString().toDoubleOrNull()
        Log.i("tag",doublePrice.toString()+ "Priceee")
        val img = Image(attachment = imageString, filename = "3.png")
        val imagelist = listOf(ImagesItem(attachment = imageString, filename = "3.png") )
       // if(!args.mylocation.isNullOrEmpty() && !args.mylocation.equals(""))

        val varian = listOf(//   args.mylocation
            Variant(price = doublePrice , option1 = locationFromMAp , sku = binding.telphoneEditText.text.toString() )
        )

        if(mytag.equals("spare")){
            producttype = myproductType
        }
        else{
            producttype = myproductTypeEquipment
        }

        var product = ProductPost(Product(title = binding.titleEditText.text.toString(), tags = mytag
            ,bodyHtml = binding.describtionEditText.text.toString(),productType = producttype ,images = imagelist, image = img
            ,templateSuffix = binding.manfactoryEditText.text.toString(), variants = varian, vendor = FirebaseAuth.getInstance().currentUser?.uid.toString()))


        Log.i("Tag", "Imgggggggg"+binding.titleEditText.text.toString())

        viewModel.postProduct(product)
    }
    private fun displayDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_progress)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(3200)
            dialog.dismiss()
            Toast.makeText(context, R.string.save, Toast.LENGTH_SHORT).show()
            val action: NavDirections = AddEquipmentSellFragmentDirections.actionAddEquipmentSellFragmentToDisplaySellerProductsFragment()
            binding.root.findNavController().navigate(action)
        }
    }
    private fun checkDataEnter() {
        if( binding.titleEditText.getText().toString().trim().equals(""))
        {
            binding.titleEditText.setError( "title is required!" )

            binding.titleEditText.setHint("please enter title")

        }
        if(binding.telphoneEditText.getText().toString().trim().equals("")){
            binding.telphoneEditText.setError( "Telephone is required!" )

            binding.telphoneEditText.setHint("please enter Telephone")


        }
        if( binding.describtionEditText.getText().toString().trim().equals(""))
        {
            binding.describtionEditText.setError( "describtion is required!" )

            binding.describtionEditText.setHint("please enter describtion")

        }
        if( binding.priceEditText.getText().toString().trim().equals(""))
        {
            binding.priceEditText.setError( "price is required!" )

            binding.priceEditText.setHint("please enter price")

        }
        if( binding.manfactoryEditText.getText().toString().trim().equals(""))
        {
            binding.manfactoryEditText.setError( "Manfactory is required!" )

            binding.manfactoryEditText.setHint("please enter manfactory")

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
                    binding.productImg.setImageURI(imageUri)

        }
    }
    private fun checkData(): Boolean {
      if((binding.manfactoryEditText.getText().toString().trim().equals(""))
          || binding.priceEditText.getText().toString().trim().equals("")
          || binding.describtionEditText.getText().toString().trim().equals("")
          || binding.titleEditText.getText().toString().trim().equals("")
          || binding.telphoneEditText.getText().toString().trim().equals(""))
                    return false
        else{
            return true
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