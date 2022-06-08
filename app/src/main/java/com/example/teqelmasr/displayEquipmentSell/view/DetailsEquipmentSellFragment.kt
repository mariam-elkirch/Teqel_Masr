package com.example.teqelmasr.displayEquipmentSell.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.displayEquipmentRent.view.DetailsEquipmentRentFragmentArgs

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_details_equipment_sell, container, false)
        var title: TextView = view.findViewById(R.id.title_txt)
        title.text= args.productsell.title
        var price: TextView = view.findViewById(R.id.price_txt)
        price.text= args.productsell.variants?.get(0)?.price.toString()
        var date: TextView = view.findViewById(R.id.date_txt)


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