package com.example.e_commerce.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentFavoritesBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class FavoritesFragment : Fragment() {

    lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val badge = bottomNavView.getOrCreateBadge(R.id.favoritesFragment)

        if (badge.number > 0) {
            badge.isVisible = true
        }

        //todo: implement logic for saving items to favorites
        badge.number++

    }

}