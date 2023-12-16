package com.po.photoalbum.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.po.photoalbum.R
import com.po.photoalbum.ui.graph.HomeRoute
import kotlinx.coroutines.delay

@Composable
fun SplashLottieScreen(
    navController: NavController,
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.welcome))
    LaunchedEffect(key1 = true) {
        delay(3000L)
        navController.popBackStack()
        navController.navigate(HomeRoute.Home.name)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(width = 200.dp, height = 100.dp),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(composition = composition)
        }
    }
}