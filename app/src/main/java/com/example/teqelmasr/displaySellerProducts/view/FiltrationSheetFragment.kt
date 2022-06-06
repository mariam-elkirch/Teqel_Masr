package com.example.teqelmasr.displaySellerProducts.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentFilterationSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FiltrationSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterationSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterationSheetBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
}