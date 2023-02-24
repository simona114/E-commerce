package com.example.e_commerce.ui

import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.data.models.product.ProductCategory
import com.example.e_commerce.data.models.product.ProductModel
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.data.adapter.ProductAdapter
import com.example.e_commerce.data.db.entity.ProductEntity
import com.example.e_commerce.ui.product.ProductViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var productAdapter: ProductAdapter? = null
    lateinit var products: List<ProductModel>


    lateinit var menuHost: MenuHost
    lateinit var menuProvider: MenuProvider

    private val viewModelProducts: ProductViewModel by viewModels()


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

//        val products = listOf(
//            ProductModel(
//                "phone",
//                800.00,
//                ProductCategory.ELECTRONICS,
//                "https://i.dummyjson.com/data/products/1/4.jpg"
//            ),
//            ProductModel("Iphone", 1800.00, ProductCategory.ELECTRONICS, "url"),
//            ProductModel("Iphone", 1800.00, ProductCategory.MEN_CLOTHING, "url"),
//            ProductModel("Jewelry", 1800.00, ProductCategory.JEWELRY, "url")
//        )


        //todo: replace with data from the server
        val pr = ProductEntity(1, "Iphone", 1800.00, "electronics", "url")
        val pr2 = ProductEntity(2, "men's jeans", 800.00, "men_clothing", "url")
        viewModelProducts.cacheProduct(pr)
        viewModelProducts.cacheProduct(pr2)

        viewModelProducts.getCachedProducts()

        viewModelProducts.productsLiveData.observe(viewLifecycleOwner) { productsList ->

            products = productsList
            productAdapter = ProductAdapter()
            productAdapter?.injectList(products)

            binding.rvProducts.apply {
                adapter = productAdapter
                layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            }


            val checkedChangeListener =
                CompoundButton.OnCheckedChangeListener { _, _ ->
                    filterList(products)
                }

            binding.apply {
                cCategoryElectronics.setOnCheckedChangeListener(checkedChangeListener)
                cCategoryJewelry.setOnCheckedChangeListener(checkedChangeListener)
                cCategoryWomenClothing.setOnCheckedChangeListener(checkedChangeListener)
                cCategoryMenClothing.setOnCheckedChangeListener(checkedChangeListener)
            }

            productAdapter = ProductAdapter()
            productAdapter?.injectList(products)

            binding.rvProducts.apply {
                adapter = productAdapter
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    private fun filterList(products: List<ProductModel>) {
        val filteredProducts = ArrayList<ProductModel>()

        val chipGroup = binding.cgProductCategories
        val selectedChipsTitles = chipGroup.children
            .filter { (it as Chip).isChecked }
            .map { (it as Chip).text.toString().lowercase() }.toList()

        val selectedCategories: MutableSet<ProductCategory> = HashSet()

        //todo:extract to string values
        selectedChipsTitles.forEach { chipTitle ->
            when (chipTitle) {
                "electronics" -> selectedCategories.add(ProductCategory.ELECTRONICS)
                "jewelry" -> selectedCategories.add(ProductCategory.JEWELRY)
                "men's clothing" -> selectedCategories.add(ProductCategory.MEN_CLOTHING)
                "women's clothing" -> selectedCategories.add(ProductCategory.WOMEN_CLOTHING)
            }
        }

        viewModelProducts.selectedProductCategories.postValue(selectedCategories)

        viewModelProducts.selectedProductCategories.observe(viewLifecycleOwner) { selectedProductCategoriesList ->

            if (selectedProductCategoriesList.isEmpty()) {
                binding.apply {
                    tvNoProducts.visibility = View.GONE
                    rvProducts.visibility = View.VISIBLE
                }
                productAdapter?.injectList(products)
            } else {
                for (product in products) {
                    if (product.category in selectedProductCategoriesList) {
                        filteredProducts.add(product)

                    }

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
                    productAdapter?.injectList(filteredProducts)
                }
            }

        }

    }


    override fun onResume() {
        super.onResume()
        viewModelProducts.selectedProductCategories.observe(viewLifecycleOwner) {
            filterList(products)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        menuHost.removeMenuProvider(menuProvider)

    }
}


