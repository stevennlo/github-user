package com.example.githubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.util.setupWithNavController

class MainActivity : AppCompatActivity() {
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            val elevation = when (destination.id) {
                R.id.userDetailFragment,
                R.id.relationAndRepoFragment -> 0f
                else -> resources.getDimension(R.dimen.elevation_level_1)
            }
            supportActionBar?.elevation = elevation
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_App)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        setContentView(binding.root)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds =
            listOf(R.navigation.users_nav, R.navigation.favorite_nav, R.navigation.settings_nav)

        val controller = binding.mainBottomNavBnv.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent,
            destinationChangedListener = destinationChangedListener
        )

        controller.observeForever {
            setupActionBarWithNavController(it)
        }
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}