package com.choegozip.presentation.main

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.choegozip.presentation.main.album.AlbumScreen
import com.choegozip.presentation.main.library.LibraryScreen
//import com.choegozip.presentation.main.play.PlayScreen

@Composable
fun MainNavHost(
    mainViewModel: MainViewModel
) {
    val TAG = "MainNavHost"

    val navController = rememberNavController()

    var isExpanded by remember { mutableStateOf(false) }

    Surface {
        Scaffold(
            topBar = {
                MainTopBar(navController)
            },
            content = { paddingValues ->
                NavHost(
                    modifier = Modifier
                        .padding(paddingValues)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                Log.d(TAG, "tap content")
                            }
                        },
                    navController = navController,
                    startDestination = MainRoute.LIBRARY.route
                ) {
                    composable(route = MainRoute.LIBRARY.route) {
                        LibraryScreen(
                            mainViewModel = mainViewModel,
                            onNavigateToAlbumScreen = {
                                navController.navigate(
                                    route = MainRoute.ALBUM.route,
                                    navOptions = navOptions {
                                        popUpTo(route = MainRoute.LIBRARY.route)
                                    }
                                )
                            },
                        )
                    }
                    composable(route = MainRoute.ALBUM.route) {
                        AlbumScreen(mainViewModel)
                    }
                }

            },
            bottomBar = {
//                PlayScreen(
//                    isExpanded = isExpanded,
//                    onChangeExpanded = { isExpanded = it }
//                )
            }
        )
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}