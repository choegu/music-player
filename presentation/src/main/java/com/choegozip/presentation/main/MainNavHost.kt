package com.choegozip.presentation.main

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import com.choegozip.presentation.main.album.AlbumScreen
import com.choegozip.presentation.main.library.LibraryScreen
import com.choegozip.presentation.main.player.BigPlayerScreen
import com.choegozip.presentation.main.player.SmallPlayerScreen

@Composable
fun MainNavHost(
    mainViewModel: MainViewModel,
    mainPlayerView: PlayerView,
) {
    val TAG = "MainNavHost"

    val navController = rememberNavController()

    var isExpanded by remember { mutableStateOf(false) }

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
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
                                        route = MainRoute.ALBUM.route.replace(
                                            oldValue = "{albumId}",
                                            newValue = it.albumId.toString()
                                        ),
                                        navOptions = navOptions {
                                            popUpTo(route = MainRoute.LIBRARY.route)
                                        },
                                    )
                                },
                            )
                        }
                        composable(
                            route = MainRoute.ALBUM.route,
                            arguments = listOf(
                                navArgument("albumId") {
                                    type = NavType.LongType
                                }
                            )
                        ) {
                            val albumId = it.arguments?.getLong("albumId")
                            albumId?.let {
                                AlbumScreen(
                                    mainViewModel = mainViewModel,
                                    albumId = albumId
                                )
                            }
                        }
                    }

                },
                bottomBar = {
                    if (!isExpanded) {
                        SmallPlayerScreen(
                            mainViewModel = mainViewModel,
                            onClickSpread = {
                                isExpanded = true
                            },
                        )
                    }
                },
            )

            if (isExpanded) {
                BigPlayerScreen(
                    mainViewModel = mainViewModel,
                    mainPlayerView = mainPlayerView,
                    onClickFold = {
                        isExpanded = false
                    },
                )
            }
        }

    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}