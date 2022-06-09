package com.example.teqelmasr.displaySparePart.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplaySparePartBinding
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparPartsViewModelFactory
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparePartsViewModel
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client


class DisplaySparePartFragment : Fragment(), OnProductClickListener {

    private val binding by lazy { FragmentDisplaySparePartBinding.inflate(layoutInflater) }
    private val args by navArgs<DisplaySparePartFragmentArgs>()
    private lateinit var sparePartsList: ArrayList<Product>
    private val sparePartsAdapter by lazy {
        DisplaySparePartsRecyclerAdapter(
            requireContext(),
            this
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory = DisplaySparPartsViewModelFactory(
                Repository.getInstance(
                    Client.getInstance(),
                    requireContext()
                )
            )
        )[DisplaySparePartsViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        Log.i("DisplaySparePartFragment", "onResume: ")
        fetchSpareParts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("DisplaySparePartFragment", "onCreateView: ")
        Log.i("ARGS start", args.filterValues?.priceStart.toString())
        Log.i("ARGS end", args.filterValues?.priceEnd.toString())
        Log.i("ARGS", " Types list args size ${args.filterValues?.types?.size}")
        setUpUI()

        fetchSpareParts()

        return binding.root
    }

    private fun setUpUI() {
        binding.apply {
            recyclerViewSpareParts.adapter = sparePartsAdapter
            recyclerViewSpareParts.hasFixedSize()
            recyclerViewSpareParts.layoutManager = LinearLayoutManager(requireContext())
            filterButton.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_displaySparePartFragment_to_sparePartsFilterBottomSheetFragment)
            }
            setUpSearch()
            refreshLayout.setOnRefreshListener {
                fetchSpareParts()
            }

        }
    }

    private fun setUpSearch() {
        binding.apply {

            searchSpareParts.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    sparePartsAdapter.filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    sparePartsAdapter.filter.filter(newText)
                    return true
                }
            })

            searchSpareParts.setOnCloseListener(SearchView.OnCloseListener() {
                Log.i("Tag", "onCreateView: setOnCloseListener")
                binding.apply {
                    noResultsImage.visibility = View.GONE
                    noResultText.visibility = View.GONE
                }
                false
            })
        }
    }

    private fun fetchSpareParts() {
        viewModel.fetchSpareParts()
        viewModel.sparePartsLiveData.observe(viewLifecycleOwner) { productsList ->
            binding.refreshLayout.isRefreshing = false
            fillSparePartsData(productsList)
        }
    }

    private fun fillSparePartsData(productsList: List<Product>) {
        if (productsList.isNullOrEmpty()) {
            binding.spareShimmer.stopShimmer()
            binding.spareShimmer.visibility = View.GONE
        }
        if (args.filterValues != null) {
            //if types is empty and price is not null -> filter with price
            if (args.filterValues!!.types.isNullOrEmpty() &&
                (args.filterValues!!.priceStart != null && args.filterValues!!.priceEnd != null)
            ) {
                sparePartsList =
                    productsList.filter {
                        it.variants!![0].price!! >= args.filterValues!!.priceStart!!
                                && it.variants!![0].price!! <= args.filterValues!!.priceEnd!!
                    } as ArrayList<Product>
                sparePartsAdapter.setData(sparePartsList)
                binding.apply {
                    searchSpareParts.visibility = View.VISIBLE
                    filterButton.visibility = View.VISIBLE
                    spareShimmer.stopShimmer()
                    spareShimmer.visibility = View.GONE
                }
            }
            //if price is null and types is not empty -> filter with type
            else if (!(args.filterValues!!.types.isNullOrEmpty()) &&
                (args.filterValues!!.priceStart == null && args.filterValues!!.priceEnd == null)) {
                sparePartsList =
                    productsList.filter { it.productType!!.toLowerCase() in args.filterValues!!.types!! } as ArrayList<Product>
                sparePartsAdapter.setData(sparePartsList)
                binding.apply {
                    searchSpareParts.visibility = View.VISIBLE
                    filterButton.visibility = View.VISIBLE
                    spareShimmer.stopShimmer()
                    spareShimmer.visibility = View.GONE
                }
            }
            //if price is not null and types is not empty -> filter with both price and type
            else if (!(args.filterValues!!.types.isNullOrEmpty()) &&
                (args.filterValues!!.priceStart != null && args.filterValues!!.priceEnd != null)
            ) {
                Log.i("TAG", "fillSparePartsData: Inside Else")

                sparePartsList =
                    productsList.filter {
                        it.productType!!.toLowerCase() in args.filterValues!!.types!!
                                && (it.variants!![0].price!! >= args.filterValues!!.priceStart!!
                                && it.variants!![0].price!! <= args.filterValues!!.priceEnd!!)
                    } as ArrayList<Product>

                sparePartsAdapter.setData(sparePartsList)
                binding.apply {
                    searchSpareParts.visibility = View.VISIBLE
                    filterButton.visibility = View.VISIBLE
                    spareShimmer.stopShimmer()
                    spareShimmer.visibility = View.GONE
                }
                Log.i("TAG", "fillSparePartsData: spare parts list size ${sparePartsList.size}")
            } else {
                Log.i("TAG", "fillSparePartsData: ${productsList.size}")
                sparePartsAdapter.setData(productsList)
                binding.apply {
                    searchSpareParts.visibility = View.VISIBLE
                    filterButton.visibility = View.VISIBLE
                    spareShimmer.stopShimmer()
                    spareShimmer.visibility = View.GONE
                }
            }

            /*else {
                Log.i("TAG", "fetchSpareParts: ${productItem.size}")
                sparePartsAdapter.setData(productItem)
                binding.apply {
                    searchSpareParts.visibility = View.VISIBLE
                    filterButton.visibility = View.VISIBLE
                    spareShimmer.stopShimmer()
                    spareShimmer.visibility = View.GONE
                }
            }*/

            /*if (args.filterValues!!.priceStart != null && args.filterValues!!.priceEnd != null ){
                sparePartsList =
                    productItem.filter { it.variants!![0].price!! >= args.filterValues!!.priceStart!!
                            && it.variants!![0].price!! <= args.filterValues!!.priceEnd!!  } as ArrayList<Product>
                sparePartsAdapter.setData(sparePartsList)
            }*/

        } else {
            Log.i("TAG", "fillSparePartsData: ${productsList.size}")
            sparePartsAdapter.setData(productsList)
            binding.apply {
                searchSpareParts.visibility = View.VISIBLE
                filterButton.visibility = View.VISIBLE
                spareShimmer.stopShimmer()
                spareShimmer.visibility = View.GONE
            }
        }
    }

    private fun filterData(productItem: List<Product>) {
        if (!(args.filterValues!!.types.isNullOrEmpty())) {
            sparePartsList =
                productItem.filter { it.productType!!.toLowerCase() in args.filterValues!!.types!! } as ArrayList<Product>
            sparePartsAdapter.setData(sparePartsList)
            binding.apply {
                searchSpareParts.visibility = View.VISIBLE
                filterButton.visibility = View.VISIBLE
                spareShimmer.stopShimmer()
                spareShimmer.visibility = View.GONE
            }
        }
    }

    override fun onProductClick(product: Product) {
        val action =
            DisplaySparePartFragmentDirections.actionDisplaySparePartFragmentToDetailsSparePartFragment2(
                product
            )
        binding.root.findNavController().navigate(action)
        Log.i("TAG", "${product.title} Inside onProductClick")
    }

    override fun onEmptyList(searchKey: String) {
        binding.apply {
            noResultsImage.visibility = View.VISIBLE
            noResultText.text = "No Results for your search \"$searchKey\""
            noResultText.visibility = View.VISIBLE
        }
    }

    override fun onFullList() {
        binding.apply {
            noResultsImage.visibility = View.GONE
            noResultText.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i("DisplaySparePartFragment", "onPause: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("DisplaySparePartFragment", "onDestroy: ")
    }

}