package com.po.photoalbum.ui.detail


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.po.photoalbum.R
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel?,
    onNavigate: () -> Unit
) {
    val detailUiState = detailViewModel?.detailUiState ?: DetailUiState()
    val photoUrl = detailUiState.photo
    val context = LocalContext.current
    val snackState = remember {
        SnackbarHostState()
    }
    val downloadSuccess = stringResource(id = R.string.download_success)
    val downloadClickState = remember { mutableStateOf(true) }

    fun downloadFile(context: Context, url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading$fileName")
            .setDescription("Downloading$fileName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(context, downloadSuccess, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(key1 = Unit) {
        println(photoUrl)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onNavigate.invoke() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "back action"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val fileName = "moep${Random.nextInt()}.jpg"
                            downloadFile(context, photoUrl, fileName)
                            downloadClickState.value = !downloadClickState.value
                        },
                        enabled = downloadClickState.value
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackState)
        }
    ) {
        ImageContent(
            url = photoUrl,
            paddingValues = it
        )
    }
}

@Composable
fun ImageContent(
    paddingValues: PaddingValues,
    url: String,
    minFloat: Float = 3f
) {
    val scale = remember { mutableFloatStateOf(1f) }
    val rotationState = remember { mutableFloatStateOf(1f) }

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .animateContentSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, rotation ->
                    scale.value *= zoom
                    rotationState.value += rotation
                }
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .animateContentSize()
                .align(Alignment.Center)
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = maxOf(0.5f, minOf(minFloat, scale.value)),
                    scaleY = maxOf(0.5f, minOf(minFloat, scale.value))
                    // rotationZ = rotationState.value
                )
        )
    }
}
