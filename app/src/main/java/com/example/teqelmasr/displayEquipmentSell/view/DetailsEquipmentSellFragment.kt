package com.example.teqelmasr.displayEquipmentSell.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.addEquipmentSell.view.AddEquipmentSellFragmentDirections
import com.example.teqelmasr.authentication.login.LoginActivity
import com.example.teqelmasr.displayEquipmentRent.view.DetailsEquipmentRentFragmentArgs
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.model.ContactInfo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsEquipmentSellFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsEquipmentSellFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val args by navArgs<DetailsEquipmentSellFragmentArgs>()
    private val user = Firebase.auth.currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_details_equipment_sell, container, false)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        var title: TextView = view.findViewById(R.id.title_txt)
        title.text= args.productsell.title
        var price: TextView = view.findViewById(R.id.price_txt)
        price.text= args.productsell.variants?.get(0)?.price.toString()
        var date: TextView = view.findViewById(R.id.date_txt)
        var show: Button = view.findViewById(R.id.showButton)
        show.setOnClickListener{
            if (user != null) {
                Log.i("TAG", " User is signed in")
                Log.i("tag",args.productsell.variants?.get(0)?.title.toString()+"args title")
                var contact = ContactInfo(args.productsell.variants?.get(0)?.sku.toString(),args.productsell.variants?.get(0)?.title.toString())
                val action = DetailsEquipmentSellFragmentDirections.actionDetailsEquipmentSellFragmentToContactInfoFragment(contact,
                    Constants.SELL_SOURCE)
                view.findNavController().navigate(action)
            }else {
                Snackbar.make(
                    view,
                    getString(R.string.loginContactInfo),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.login)) {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }.setDuration(6000).show()
            }

        }
        val dateInString = args.productsell.updated_at
        if (dateInString != null) {
            val dateAfterCut = dateInString.substringBefore('T')
            date.text = "${dateAfterCut}"
        }



        var category = view.findViewById<TextView>(R.id.category_txt)
        category.text = "Equipment For Sell"
        var type = view.findViewById<TextView>(R.id.type_txt)
        type.text = args.productsell.productType
        var manufactor = view.findViewById<TextView>(R.id.vendor_txt)
        manufactor.text = args.productsell.templateSuffix
        var description = view.findViewById<TextView>(R.id.product_desc)
        description.text = args.productsell.bodyHtml
        var productImg = view.findViewById<ImageView>(R.id.image_item)
        Glide.with(activity?.baseContext!!).load(args.productsell.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(productImg)

        return view
    }
}