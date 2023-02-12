package com.example.e_commerce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.data.ProductAdapter
import com.example.e_commerce.data.ProductCategory
import com.example.e_commerce.data.models.ProductModel
import com.example.e_commerce.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

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
            ProductModel("phone", 800.00, ProductCategory.ELECTRONICS, "https://i.dummyjson.com/data/products/1/4.jpg"),
            ProductModel("Iphone", 1800.00, ProductCategory.ELECTRONICS, "url"),
            ProductModel("Iphone", 1800.00, ProductCategory.ELECTRONICS, "url"),
            ProductModel("Iphone", 1800.00, ProductCategory.ELECTRONICS, "url")
        )

        repoAdapter = ProductAdapter()
        repoAdapter?.injectList(products)

        binding.rvProducts.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(activity,  LinearLayoutManager.HORIZONTAL, false)
        }
    }


}