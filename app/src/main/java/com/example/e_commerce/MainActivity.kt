package com.example.e_commerce

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.e_commerce.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setToolbar()
        setNavigation()
    }

    private fun setNavigation() {
        val bottomNavView: BottomNavigationView = binding.bottomNavigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

         navController = navHostFragment.navController

        //Enable navigation via back arrow on phone
        navController.enableOnBackPressed(true)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.cartFragment, R.id.favoritesFragment, R.id.profileFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)

        //todo: implement logic for saving items to favorites
        val badgeFavorites = bottomNavView.getOrCreateBadge(R.id.favoritesFragment)
        badgeFavorites.number = 1


        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

            when (destination.id) {

                R.id.homeFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE

                }
                R.id.cartFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                R.id.favoritesFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                R.id.profileFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.GONE

                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setToolbar(){
        val mToolbar = binding.mToolbar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

    }
}