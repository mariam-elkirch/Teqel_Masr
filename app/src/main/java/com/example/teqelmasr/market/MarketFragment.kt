package com.example.teqelmasr.market

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.ActivityRegisterationBinding
import com.example.teqelmasr.databinding.FragmentMarketBinding


class MarketFragment : Fragment() {

    private val binding by lazy { FragmentMarketBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

}