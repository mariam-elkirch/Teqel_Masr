package com.example.teqelmasr.displayEquipmentRent.view

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsEquipmentRentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsEquipmentRentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val args by navArgs<DetailsEquipmentRentFragmentArgs>()

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
        val view:View = inflater.inflate(R.layout.fragment_details_equipment_rent, container, false)
        var title:TextView = view.findViewById(R.id.title_txt)
        title.text= args.product.title
        var price:TextView = view.findViewById(R.id.price_txt)
        price.text= args.product.variants?.get(0)?.price.toString()
        var date:TextView = view.findViewById(R.id.date_txt)
        date.text= args.product.updated_at.toString()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsEquipmentRentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailsEquipmentRentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}