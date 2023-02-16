package com.example.e_commerce

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.CompoundButtonCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.data.ProductAdapter
import com.example.e_commerce.data.ProductCategory
import com.example.e_commerce.data.models.ProductModel
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var repoAdapter: ProductAdapter? = null

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
}


