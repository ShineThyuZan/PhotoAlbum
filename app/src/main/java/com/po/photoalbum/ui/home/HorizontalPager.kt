package com.po.photoalbum.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import coil.compose.rememberImagePainter
import com.po.photoalbum.R
import com.po.photoalbum.ui.theme.resources.dimen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
data class Image(val imageUrl: Int)
val images = listOf(
    Image(R.drawable.screen1),
    Image(R.drawable.screen2),
    Image(R.drawable.screen3),
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerView(images: List<Image>) {
    val pagerState = rememberPagerState(pageCount = { images.size }) // Stores the current page
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) { // Runs once when the composable is first called
        while (true) {
            delay(3000) // Delay between scrolls
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + 1) % images.size,
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimen.base_10x)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(
                beyondBoundsPageCount = images.size,
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = rememberImagePainter(images[page].imageUrl),
                    contentDescription = null // Set content description if needed
                )
            }
        }
    }
}

