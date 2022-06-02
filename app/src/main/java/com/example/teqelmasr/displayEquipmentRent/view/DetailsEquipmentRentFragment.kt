package com.example.teqelmasr.displayEquipmentRent.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R

class DetailsEquipmentRentFragment : Fragment() {
    private val args by navArgs<DetailsEquipmentRentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_details_equipment_rent, container, false)
        var title:TextView = view.findViewById(R.id.title_txt)
        title.text= args.product.title
        var price:TextView = view.findViewById(R.id.price_txt)
        price.text= args.product.variants?.get(0)?.price.toString()
        var date:TextView = view.findViewById(R.id.date_txt)


        val dateInString = args.product.updated_at
        if (dateInString != null) {
            val dateAfterCut = dateInString.substringBefore('T')
            date.text = "${dateAfterCut}"
        }



        var category = view.findViewById<TextView>(R.id.category_txt)
        category.text = "Equipment For Rent"
        var type = view.findViewById<TextView>(R.id.type_txt)
        type.text = args.product.productType
        var manufactor = view.findViewById<TextView>(R.id.vendor_txt)
        manufactor.text = args.product.templateSuffix
        var description = view.findViewById<TextView>(R.id.product_desc)
        description.text = args.product.bodyHtml
        var productImg = view.findViewById<ImageView>(R.id.image_item)
        Glide.with(activity?.baseContext!!).load(args.product.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(productImg)
        
        return view
    }
}