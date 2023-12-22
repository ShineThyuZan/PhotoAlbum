package com.po.photoalbum.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.po.photoalbum.ui.Resources
import com.po.photoalbum.ui.common.CustomVerticalSpacer
import com.po.photoalbum.ui.detail.DetailViewModel
import com.po.photoalbum.ui.model.PhotoAlbumsDTO
import com.po.photoalbum.ui.theme.resources.dimen
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel?,
    detailViewModel: DetailViewModel?,
    navToDetailPage: () -> Unit,
    navToLoginPage: () -> Unit,
) {
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()

    var openDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = homeViewModel?.hasUser) {
        if (homeViewModel?.hasUser == false) {
            navToLoginPage.invoke()
        }
    }
    LaunchedEffect(key1 = Unit) {
        homeViewModel?.loadPhotoAlbum()
    }
    AnimatedVisibility(visible = openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text(
                    "Are you sure to logout?",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "When logout, you need to remember your name and password. Remember? ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        homeViewModel?.signOut()
                        navToLoginPage.invoke()
                        openDialog = false
                    },
                    shape = ButtonDefaults.filledTonalShape,
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        "Logout",
                        color = MaterialTheme.colorScheme.surfaceBright
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text(
                        "Cancel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {},
                actions = {
                    IconButton(onClick = {
                        openDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text("Photo Album")
                })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(bottom = MaterialTheme.dimen.base)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                HorizontalPagerView(images)
            }
            Text(
                modifier = Modifier.padding(MaterialTheme.dimen.base_2x),
                text = "Your Photo Album"
            )
            when (homeUiState.photoAlbumsDTOList) {
                is Resources.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
                is Resources.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(MaterialTheme.dimen.tiny)
                    ) {
                        items(homeUiState.photoAlbumsDTOList.data ?: emptyList()) { photos ->
                            PhotoItem(
                                photoAlbumsDTO = photos,
                                onClick = {
                                    detailViewModel!!.saveUrl(url = it)
                                    navToDetailPage.invoke()
                                }
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        text = homeUiState.photoAlbumsDTOList.throwable?.localizedMessage
                            ?: "Unknown Error",
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoItem(
    photoAlbumsDTO: PhotoAlbumsDTO,
    onClick: (url: String) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(MaterialTheme.dimen.tiny)
            .height(250.dp)
            .clickable {
                onClick(photoAlbumsDTO.url)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        border = BorderStroke(width = 0.5.dp, color = Color.LightGray)
    ) {
        AsyncImage(
            model = photoAlbumsDTO.url,
            contentDescription = "Avatar Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick(photoAlbumsDTO.url)
                }
        )
    }
}

private fun formatDate(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("MM-dd-yy hh:mm", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}



