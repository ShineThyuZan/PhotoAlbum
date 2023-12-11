package com.po.photoalbum.ui.detail


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel?,
    photoId: String,
    onNavigate: () -> Unit
) {
    val detailUiState = detailViewModel?.detailUiState ?: DetailUiState()
    val isFormsNotBlank = detailUiState.photo.isNotBlank() &&
            detailUiState.title.isNotBlank()
    val isPhotoIdNotBlank = photoId.isNotBlank()
    val icon = if (isPhotoIdNotBlank) Icons.Default.Refresh else Icons.Default.Check
    LaunchedEffect(key1 = Unit) {
        if (isPhotoIdNotBlank) {
            detailViewModel?.getPhoto(photoId = photoId)
        } else {
            detailViewModel?.resetState()
        }
    }

    val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(visible = isFormsNotBlank) {
                FloatingActionButton(onClick = {
                    if (isPhotoIdNotBlank) {
                        detailViewModel?.updateNote(photoId)
                    } else {
                        detailViewModel?.addPhoto()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = null
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (detailUiState.photoAddedStatus) {
                scope.launch {
                    detailViewModel?.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }
            if (detailUiState.updatePhotoStatus) {
                scope.launch {
                    detailViewModel?.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = 8.dp
                )
            ) {

            }
        }
        Column {
            OutlinedTextField(
                value = detailUiState.title,
                onValueChange = {
                    detailViewModel?.onTitleChange(it)
                },
                label = { Text(text = "Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = detailUiState.photo,
                onValueChange = {
                    detailViewModel?.onPhotoChange(it)
                },
                label = { Text(text = "Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

    }
}
