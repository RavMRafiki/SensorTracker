package com.example.sensortracker.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sensortracker.data.repository.CsvSensorStorageRepository
import com.example.sensortracker.data.repository.InMemorySettingsRepository
import com.example.sensortracker.data.sensor.AndroidSensorFactory
import com.example.sensortracker.presentation.graphs.GraphsScreen
import com.example.sensortracker.presentation.navigation.Screen
import com.example.sensortracker.presentation.sensor.SensorScreen
import com.example.sensortracker.presentation.sensor.SensorViewModel
import com.example.sensortracker.presentation.settings.SettingsScreen
import com.example.sensortracker.presentation.settings.SettingsViewModel
import com.example.sensortracker.ui.theme.BottomNavBackground
import com.example.sensortracker.ui.theme.DeepNavyBlue
import com.example.sensortracker.ui.theme.SecondaryText
import com.example.sensortracker.ui.theme.SensorTrackerTheme
import com.example.sensortracker.ui.theme.SoftIceBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SensorTrackerTheme {
                val settingsRepository = remember { InMemorySettingsRepository(applicationContext) }
                val sensorFactory = remember { AndroidSensorFactory(applicationContext) }
                val storageRepository = remember { CsvSensorStorageRepository(applicationContext) }

                val sensorViewModel = viewModel<SensorViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return SensorViewModel(
                                sensorFactory = sensorFactory,
                                settingsRepository = settingsRepository,
                                storageRepository = storageRepository
                            ) as T
                        }
                    }
                )

                val settingsViewModel = viewModel<SettingsViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return SettingsViewModel(
                                settingsRepository = settingsRepository
                            ) as T
                        }
                    }
                )

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = SoftIceBlue,
                    bottomBar = {
                        NavigationBar(
                            containerColor = BottomNavBackground,
                            contentColor = DeepNavyBlue
                        ) {
                            Screen.items.forEach { screen ->
                                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                            contentDescription = screen.title
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = screen.title,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        selectedTextColor = DeepNavyBlue,
                                        indicatorColor = DeepNavyBlue,
                                        unselectedIconColor = SecondaryText,
                                        unselectedTextColor = SecondaryText
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Main.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Main.route) {
                            SensorScreen(viewModel = sensorViewModel)
                        }
                        composable(Screen.Graphs.route) {
                            GraphsScreen()
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(viewModel = settingsViewModel)
                        }
                    }
                }
            }
        }
    }
}
