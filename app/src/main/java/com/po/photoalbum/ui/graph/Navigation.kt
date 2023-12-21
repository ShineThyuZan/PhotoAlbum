package com.po.photoalbum.ui.graph

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
import com.po.photoalbum.ui.auth.LoginScreen
import com.po.photoalbum.ui.auth.LoginViewModel
import com.po.photoalbum.ui.auth.SignUpScreen
import com.po.photoalbum.ui.detail.DetailScreen
import com.po.photoalbum.ui.detail.DetailViewModel
import com.po.photoalbum.ui.home.HomeScreen
import com.po.photoalbum.ui.home.HomeViewModel
import com.po.photoalbum.ui.splash.SplashLottieScreen

enum class LoginRoutes {
    Splash,
    Signup,
    SignIn
}

enum class HomeRoute {
    Splash,
    Home,
    Detail
}

enum class NestedRoute {
    Splash,
    Main,
    Login
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavGraph(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    detailViewModel: DetailViewModel,
    homeViewModel: HomeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NestedRoute.Splash.name,
    ) {
        composable(route = NestedRoute.Splash.name) {
            /** Lottie Animation*/
            SplashLottieScreen(navController = navController)
        }
        authGraph(navController = navController, loginViewModel = loginViewModel)
        homeGraph(
            navController = navController,
            homeViewModel = homeViewModel,
            detailViewModel = detailViewModel
        )
    }
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
                detailViewModel = detailViewModel,
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
            route = HomeRoute.Detail.name,
            arguments = listOf(navArgument("url") {
                type = NavType.StringType
                defaultValue = ""
            }
            )) { entry ->
            DetailScreen(
                detailViewModel = detailViewModel,
                // photoId = entry.arguments?.getString("url") as String
            ) {
                navController.navigateUp()
            }
        }
    }
}