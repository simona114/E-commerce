package com.example.e_commerce.ui.product.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.R
import com.example.e_commerce.data.models.product.ProductModel
import com.example.e_commerce.databinding.ItemProductBinding

class ProductAdapter() :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(), Filterable {
    var productList = listOf<ProductModel>()
    var productListFiltered = listOf<ProductModel>()

    inner class ProductViewHolder(private val productsBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(productsBinding.root) {
        fun bindProductItem(product: ProductModel) {
            productsBinding.apply {
                tvProductTitle.text = product.title
                tvProductPrice.text = product.price.toString()

                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .into(ivProductImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = productList[position]
        holder.bindProductItem(currentProduct)
    }

    override fun getItemCount() = productList.size

    fun injectList(productList: List<ProductModel>) {
        this.productList = productList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
                Log.d("filterSearch", "performFiltering: $filterPattern")
                productListFiltered = if (filterPattern.isEmpty()) productList else {
                    val tempFilteredList = ArrayList<ProductModel>()
                    productList
                        .filter {
                            it.title.lowercase().contains(filterPattern)
                        }
                        .forEach { tempFilteredList.add(it) }
                    tempFilteredList

                }

                return FilterResults().apply { values = productListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                productListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as List<ProductModel>

                Log.d("filterSearch", "publishResults: ${productListFiltered.size}")

                injectList(productListFiltered)

            }
        }
    }

}