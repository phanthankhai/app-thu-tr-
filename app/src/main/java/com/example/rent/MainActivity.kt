package com.example.rent

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.rent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavBaseHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_height)
        val bottomNavInitialPaddingBottom = binding.bottomNavigation.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            binding.bottomNavigation.updateLayoutParams {
                height = bottomNavBaseHeight + systemBars.bottom
            }
            binding.bottomNavigation.setPadding(
                binding.bottomNavigation.paddingLeft,
                binding.bottomNavigation.paddingTop,
                binding.bottomNavigation.paddingRight,
                bottomNavInitialPaddingBottom + systemBars.bottom,
            )
            insets
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.isVisible = destination.id in MAIN_DESTINATIONS
        }
    }

    companion object {
        private val MAIN_DESTINATIONS = setOf(
            R.id.homeFragment,
            R.id.roomsFragment,
            R.id.tenantsFragment,
            R.id.billsFragment,
            R.id.profileFragment,
        )
    }
}
