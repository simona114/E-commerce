package com.example.e_commerce.ui.home

import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.data.adapter.ProductAdapter
import com.example.e_commerce.data.db.entity.ProductEntity
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.ui.product.BaseProductViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var productAdapter: ProductAdapter? = null


    lateinit var menuHost: MenuHost
    lateinit var menuProvider: MenuProvider

    private val viewModelHome: HomeViewModel by viewModels()
    private val viewModelProduct: BaseProductViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuHost = requireActivity()
        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                menu.findItem(R.id.miSearch).isVisible = true
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.miSearch -> {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductsFragment())
                        true
                    }
                    else -> false
                }
            }
        }
        menuHost.addMenuProvider(menuProvider)


        //todo: replace with data from the server
        val pr = ProductEntity(
            1,
            "women's skirt",
            12.99,
            "women's clothing",
            "https://fakestoreapi.com/img/61pHAEJ4NML._AC_UX679_.jpg"
        )
        val pr2 = ProductEntity(
            2,
            "men's jeans",
            55.99,
            "men's clothing",
            "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg"
        )
        viewModelProduct.cacheProduct(pr)
        viewModelProduct.cacheProduct(pr2)


        val chipGroup = binding.cgProductCategories

        val checkedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, _ ->

                val selectedCategoryTitles = chipGroup.children
                    .filter { (it as Chip).isChecked }
                    .map { (it as Chip).text.toString() }.toList()

                viewModelHome.selectedProductCategories.postValue(selectedCategoryTitles)
            }

        binding.apply {
            cCategoryElectronics.setOnCheckedChangeListener(checkedChangeListener)
            cCategoryJewelry.setOnCheckedChangeListener(checkedChangeListener)
            cCategoryWomenClothing.setOnCheckedChangeListener(checkedChangeListener)
            cCategoryMenClothing.setOnCheckedChangeListener(checkedChangeListener)
        }


        viewModelHome.selectedProductCategories.observe(viewLifecycleOwner) { selectedProductCategoriesList ->
            viewModelHome.getCachedProductsFromSelectedCategories()

            if (selectedProductCategoriesList.isEmpty()) {
                binding.apply {
                    rvProducts.visibility = View.VISIBLE
                    tvNoProducts.visibility = View.GONE
                }

            }
        }


        viewModelHome.filteredProductsLiveData.observe(viewLifecycleOwner) { filteredProducts ->

            productAdapter = ProductAdapter()

            productAdapter?.injectList(filteredProducts)

            binding.rvProducts.apply {
                adapter = productAdapter
                layoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }

            if (filteredProducts.isEmpty()) {
                binding.apply {
                    rvProducts.visibility = View.GONE
                    tvNoProducts.visibility = View.VISIBLE
                }

            } else {
                binding.apply {
                    rvProducts.visibility = View.VISIBLE
                    tvNoProducts.visibility = View.GONE
                }
                productAdapter = ProductAdapter()
                productAdapter?.injectList(filteredProducts)

                binding.rvProducts.apply {
                    adapter = productAdapter
                    layoutManager = LinearLayoutManager(
                        activity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        viewModelHome.getCachedProductsFromSelectedCategories()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        menuHost.removeMenuProvider(menuProvider)

    }
}


