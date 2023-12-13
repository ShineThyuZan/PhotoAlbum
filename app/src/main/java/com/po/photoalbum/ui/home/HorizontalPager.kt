package com.po.photoalbum.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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

    HorizontalPager(
        beyondBoundsPageCount = images.size,
        state = pagerState
    ) { page ->
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                ),
            shape = RoundedCornerShape(MaterialTheme.dimen.base),
            ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = rememberImagePainter(images[page].imageUrl),
                contentScale = ContentScale.Crop,
                contentDescription = null // Set content description if needed
            )
        }
    }
}

