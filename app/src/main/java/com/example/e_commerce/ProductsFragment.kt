package com.example.e_commerce

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce.data.ProductAdapter
import com.example.e_commerce.data.ProductCategory
import com.example.e_commerce.data.models.ProductModel
import com.example.e_commerce.databinding.FragmentProductsBinding


class ProductsFragment : Fragment() {
    lateinit var binding: FragmentProductsBinding

    lateinit var menuHost: MenuHost
    lateinit var menuProvider: MenuProvider

    private var productAdapter: ProductAdapter? = null
    lateinit var products: List<ProductModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo:implement filtering
        menuHost = requireActivity()
        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.products_menu, menu)

                setSearchView(menu)

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


        products = listOf(
            ProductModel(
                "phone",
                800.00,
                ProductCategory.ELECTRONICS,
                "https://i.dummyjson.com/data/products/1/4.jpg"
            ),
            ProductModel("Iphone", 1800.00, ProductCategory.ELECTRONICS, "url"),
            ProductModel("Iphone", 1800.00, ProductCategory.MEN_CLOTHING, "url"),
            ProductModel("Jewelry", 1800.00, ProductCategory.JEWELRY, "url"),
            ProductModel("Jewelry", 1800.00, ProductCategory.JEWELRY, "url"),
            ProductModel("Jewelry", 1800.00, ProductCategory.JEWELRY, "url"),
            ProductModel("Jewelry", 1800.00, ProductCategory.JEWELRY, "url")
        )

        productAdapter = ProductAdapter()
        productAdapter?.injectList(products)

        binding.rvProducts.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        }

    }

    private fun setSearchView(menu: Menu) {
        val searchView =
            menu.findItem(R.id.miSearchProducts).actionView as androidx.appcompat.widget.SearchView


        searchView.apply {
            queryHint = "Search for a product..."
            isIconified = false;
            maxWidth = Int.MAX_VALUE

            setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    productAdapter?.getFilter()?.filter(query)
                    productAdapter?.injectList(products)
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    productAdapter?.getFilter()?.filter(query)
                    productAdapter?.injectList(products)
                    return false
                }

            })
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        menuHost.removeMenuProvider(menuProvider)

    }

    private fun changeSearchViewTextColor(view: View?) {
        if (view != null) {
            if (view is TextView) {
                view.setTextColor(resources.getColor(R.color.azureish_white))
                return
            } else if (view is ViewGroup) {
                val viewGroup = view
                for (i in 0 until viewGroup.childCount) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i))
                }
            }
        }
    }

}