package com.po.photoalbum.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.po.photoalbum.ui.Resources
import com.po.photoalbum.ui.StorageRepository
import com.po.photoalbum.ui.model.PhotoAlbumsDTO
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())

    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()

    private val userId: String
        get() = repository.getUserId()

    fun loadPhotoAlbum() {
        Log.d("user.user.id", userId)
        if (hasUser) {
            if (userId.isNotBlank())
                getUserNotes(userId = userId)
            else {
                homeUiState = homeUiState.copy(
                    photoAlbumsDTOList = Resources.Error(
                        throwable = Throwable(message = "User is not login")
                    )
                )
            }
        }
    }

    private fun getUserNotes(userId: String) = viewModelScope.launch {
        repository.getUserPhotoAlbum(userId = userId).collect {
            Log.d("list.album", it.data.toString())
            homeUiState = homeUiState.copy(photoAlbumsDTOList = it)
        }
    }

    fun deletePhoto(photoId: String) = repository.deletePhoto(photoId) {
        homeUiState = homeUiState.copy(noteDeletedStatus = it)
    }

    fun signOut() = repository.signOut()
}


data class HomeUiState(
    val photoAlbumsDTOList: Resources<List<PhotoAlbumsDTO>> = Resources.Loading(),
    val noteDeletedStatus: Boolean = false

)