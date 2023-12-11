package com.po.photoalbum.ui.detail

import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.po.photoalbum.ui.common.StorageRepository
import com.po.photoalbum.ui.model.PhotoAlbums

class DetailViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var detailUiState by mutableStateOf(DetailUiState())
        private set

    fun onTitleChange(title: String) {
        detailUiState = detailUiState.copy(
            title = title
        )
    }

    fun onPhotoChange(photo: String) {
        detailUiState = detailUiState.copy(
            photo = photo
        )
    }

    private val hasUser: Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()

    fun addPhoto() {
        if (hasUser) {
            repository.addNote(
                userId = user!!.uid,
                title = detailUiState.title,
                description = detailUiState.photo,
                timestamp = Timestamp.now()
            ) {
                detailUiState = detailUiState.copy(
                    photoAddedStatus = it
                )
            }
        }
    }

    fun setEditFields(photo: PhotoAlbums) {
        detailUiState = detailUiState.copy(
            title = photo.title,
            photo = photo.description,
        )
    }

    fun getPhoto(photoId: String) {
        repository.getPhotos(
            photoId = photoId,
            onError = {}
        ) { photoAlbum ->
            detailUiState = detailUiState.copy(
                selectedNote = photoAlbum
            )
            detailUiState.selectedNote?.let { it1 -> setEditFields(it1) }
        }
    }

    fun updateNote(
        photoId: String
    ) {
        repository.updatePhoto(
            title = detailUiState.title,
            photo = detailUiState.photo,
            photoId = photoId,
        ) {
            detailUiState = detailUiState.copy(
                updatePhotoStatus = it
            )
        }
    }

    fun resetNoteAddedStatus() {
        detailUiState = detailUiState.copy(
            photoAddedStatus = false,
            updatePhotoStatus = false
        )
    }

    fun resetState() {
        detailUiState = DetailUiState()
    }

}

data class DetailUiState(
    val photo: String = "",
    val title: String = "",
    val selectedNote: PhotoAlbums? = null,
    val photoAddedStatus: Boolean = false,
    val updatePhotoStatus: Boolean = false,
    val selectedPhoto: PhotoAlbums? = null
)