package com.example.teqelmasr.editSellerProduct.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.teqelmasr.R

class MySpinnerAdapter(context: Context, resource: Int, objects: Array<String>):
    ArrayAdapter<String>(context, resource, objects) {

    private val values: Array<String> = objects
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return values.size
    }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }
    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View{
        val row = LayoutInflater.from(parent.context).inflate(R.layout.custom_spinner, parent, false)
        val label: TextView = row.findViewById(R.id.value)
        label.text = values[position]
        return row
    }


}