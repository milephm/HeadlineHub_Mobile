package com.example.news.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import com.example.news.R
import com.google.android.material.bottomnavigation.BottomNavigationView

@AndroidEntryPoint // Required for Hilt injection in any child fragments
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the activity content view
        setContentView(R.layout.activity_main)

        // Find the NavHostFragment and retrieve its NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Bottom navigation bar
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.splashScreenFragment) {
                // Hide the navigation bar on the splash screen
                bottomNav.visibility = View.GONE
            } else {
                // Show it on all other screens (NewsList, SavedNews)
                bottomNav.visibility = View.VISIBLE
            }
        }
        bottomNav.setupWithNavController(navController)

        // Navigate to the NewsListFragment
        navController.navigate(R.id.splashScreenFragment)

        // Hide the ActionBar
        supportActionBar?.hide()

    }
}