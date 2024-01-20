package com.example.observationapp.dashboard.presentationlayer.ui.activity

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.observationapp.R
import com.example.observationapp.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mToolbarDashboard)
        navController = findNavController(R.id.navHost)
        //navController = (supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment).navController//findNavController(R.id.navHost)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    //below code added --- after theme change app was crashing when user is in
    private fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
        val destinationId = currentDestination?.getAction(resId)?.destinationId.orEmpty()
        currentDestination?.let { node ->
            val currentNode = when (node) {
                is NavGraph -> node
                else -> node.parent
            }
            if (destinationId != 0) {
                currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
            }
        }
    }

    private fun Int?.orEmpty(default: Int = 0): Int {
        return this ?: default
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}