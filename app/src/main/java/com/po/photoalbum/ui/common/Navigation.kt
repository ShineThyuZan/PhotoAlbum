package com.po.photoalbum.ui.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.po.photoalbum.ui.detail.DetailScreen
import com.po.photoalbum.ui.detail.DetailViewModel
import com.po.photoalbum.ui.home.HomeScreen
import com.po.photoalbum.ui.home.HomeViewModel

enum class LoginRoutes {
    Signup,
    SignIn
}

enum class HomeRoute {
    Home,
    Detail
}

enum class NestedRoute {
    Main,
    Login
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    detailViewModel: DetailViewModel,
    homeViewModel: HomeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NestedRoute.Main.name
    ) {
        authGraph(navController = navController, loginViewModel = loginViewModel)
        homeGraph(
            navController = navController,
            homeViewModel = homeViewModel,
            detailViewModel = detailViewModel
        )
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    navigation(
        startDestination = LoginRoutes.SignIn.name,
        route = NestedRoute.Login.name
    ) {
        composable(route = LoginRoutes.SignIn.name) {
            LoginScreen(
                onNavToHomePage = {
                    navController.navigate(NestedRoute.Main.name) {
                        launchSingleTop = true
                        popUpTo(route = LoginRoutes.SignIn.name) {
                            inclusive = true
                        }
                    }
                },
                loginViewModel = loginViewModel

            ) {
                navController.navigate(LoginRoutes.Signup.name) {
                    launchSingleTop = true
                    popUpTo(LoginRoutes.SignIn.name) {
                        inclusive = false
                    }
                }
            }
        }
        composable(route = LoginRoutes.Signup.name) {
            SignUpScreen(
                onNavToHomePage = {
                    navController.navigate(NestedRoute.Main.name) {
                        popUpTo(LoginRoutes.Signup.name) {
                            inclusive = true
                        }
                    }
                },
                loginViewModel = loginViewModel
            ) {
                navController.navigate(LoginRoutes.SignIn.name)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel
) {
    navigation(
        startDestination = HomeRoute.Home.name,
        route = NestedRoute.Main.name
    ) {
        composable(HomeRoute.Home.name) {
            HomeScreen(
                homeViewModel = homeViewModel,
                onPhotoAlbumClick = { photoId ->
                    navController.navigate(
                        HomeRoute.Detail.name + "?=$photoId"
                    ) {
                        launchSingleTop = true
                    }
                },
                navToDetailPage = {
                    navController.navigate(HomeRoute.Detail.name)
                }) {
                navController.navigate(NestedRoute.Login.name) {
                    launchSingleTop = true
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }
        composable(
            route = HomeRoute.Detail.name + "?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                defaultValue = ""
            }
            )) { entry ->
            DetailScreen(
                detailViewModel = detailViewModel,
                photoId = entry.arguments?.getString("id") as String
            ) {
                navController.navigateUp()
            }
        }
    }
}