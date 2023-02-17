package com.example.e_commerce

import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.data.ProductAdapter
import com.example.e_commerce.data.ProductCategory
import com.example.e_commerce.data.models.ProductModel
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var repoAdapter: ProductAdapter? = null

    lateinit var menuHost: MenuHost
    lateinit var menuProvider: MenuProvider

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

        val products = listOf(
            ProductModel(
                "phone",
                800.00,
                ProductCategory.ELECTRONICS,
                "https://i.dummyjson.com/data/products/1/4.jpg"
            ),
            ProductModel("Iphone", 1800.00, ProductCategory.ELECTRONICS, "url"),
            ProductModel("Iphone", 1800.00, ProductCategory.MEN_CLOTHING, "url"),
            ProductModel("Jewelry", 1800.00, ProductCategory.JEWELRY, "url")
        )

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

        repoAdapter = ProductAdapter()
        repoAdapter?.injectList(products)

        binding.rvProducts.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun filterList(products: List<ProductModel>) {
        val filteredProducts = ArrayList<ProductModel>()

        val chipGroup = binding.cgProductCategories
        val selectedChipsTitles = chipGroup.children
            .filter { (it as Chip).isChecked }
            .map { (it as Chip).text.toString().lowercase() }.toList()

        if (selectedChipsTitles.isEmpty()) {
            binding.apply {
                tvNoProducts.visibility = View.GONE
                rvProducts.visibility = View.VISIBLE
            }
            repoAdapter?.injectList(products)
        } else {
            for (product in products) {
                if (product.category.name.lowercase() in selectedChipsTitles) {
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
                repoAdapter?.injectList(filteredProducts)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        menuHost.removeMenuProvider(menuProvider)

    }
}


