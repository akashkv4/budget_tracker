package com.test.budgettracker.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.budgettracker.R
import com.test.budgettracker.databinding.ActivityMainBinding
import com.test.budgettracker.presentation.adapter.ExpenseAdapter
import com.test.budgettracker.presentation.viewmodel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val expenseViewModel: ExpenseViewModel by viewModels()

    @Inject
    lateinit var expenseAdapter: ExpenseAdapter

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var appBar: MaterialToolbar
    private lateinit var themeSwitch: SwitchCompat

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appBar = binding.appBar
        setSupportActionBar(appBar)


        // Get SharedPreferences to store the user's theme preference
        val sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)


        // Set the theme on app start based on saved preference
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        bottomNavigation = binding.bottomNavigation
        themeSwitch = binding.themeSwitch


        // Set the Switch state according to the current theme
        themeSwitch.isChecked = isDarkMode


        // Set an onCheckedChangeListener to the SwitchCompat to handle theme toggling
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Toggle theme based on the switch's state
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                // Save the user's preference
                sharedPreferences.edit().putBoolean("isDarkMode", true).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                // Save the user's preference
                sharedPreferences.edit().putBoolean("isDarkMode", false).apply()
            }
        }

        // Set up the NavController

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


        // Setup BottomNavigationView with NavController
        bottomNavigation.setupWithNavController(navController)

        // Handle Destination Changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.recordFragment -> {
                    appBar.title = "Budget Tracker"
                    bottomNavigation.visibility = View.VISIBLE
                    appBar.setNavigationIcon(null) // No back button on record fragment
                }

                R.id.chartFragment -> {
                    appBar.title = "Chart"
                    bottomNavigation.visibility = View.VISIBLE
                    appBar.setNavigationIcon(null) // No back button on chart fragment
                }

                R.id.createRecordFragment -> {
                    appBar.title = "Create Record"
                    bottomNavigation.visibility = View.GONE
                    appBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24) // Show back button
                }

                R.id.editMonthlyBudgetFragment -> {
                    appBar.title = "Edit Monthly Budget"
                    bottomNavigation.visibility = View.GONE
                    appBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24) // Show back button
                }
            }
        }

        // Handle back button press on the AppBar
        appBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun updateAppBarTitle(title: String) {
        appBar.title = title
    }


}