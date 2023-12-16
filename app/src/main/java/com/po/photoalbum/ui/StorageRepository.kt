package com.po.photoalbum.ui

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.po.photoalbum.ui.model.PhotoAlbumsDTO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val PHOTO_AlBLUM_COLLECTION_REF = "photo_album"

class StorageRepository {
    fun user() = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()
    private val photoRef: CollectionReference = Firebase.firestore.collection(
        PHOTO_AlBLUM_COLLECTION_REF
    )

    fun getUserPhotoAlbum(
        userId: String
    ): Flow<Resources<List<PhotoAlbumsDTO>>> = callbackFlow {
        var snapShotStateListener: ListenerRegistration? = null
        try {
            snapShotStateListener = photoRef
                //.orderBy("timeStamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val photos = snapshot.toObjects(PhotoAlbumsDTO::class.java)
                        Resources.Success(data = photos)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }
        } catch (e: Exception) {
            trySend(Resources.Error(throwable = e.cause))
            e.printStackTrace()
        }
        awaitClose {
            snapShotStateListener?.remove()
        }
    }

    fun getPhotos(
        photoId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (PhotoAlbumsDTO) -> Unit
    ) {
        photoRef.document(photoId)
            .get()
            .addOnSuccessListener {
                it?.toObject(PhotoAlbumsDTO::class.java)?.let { it1 -> onSuccess.invoke(it1) }
            }
            .addOnFailureListener { result ->
                onError.invoke(result.cause)
            }
    }

    fun addNote(
        userId: String,
        title: String,
        timestamp: com.google.firebase.Timestamp,
        onComplete: (Boolean) -> Unit
    ) {
        val photoId = photoRef.document().id
        val photo = PhotoAlbumsDTO(
            userId = userId,
            title,
            timestamp,
            documentId = photoId
        )
        photoRef
            .document(photoId)
            .set(photo)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    fun deletePhoto(photoId: String, onComplete: (Boolean) -> Unit) {
        photoRef.document(photoId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    fun updatePhoto(
        title: String,
        photo: String,
        photoId: String,
        onResult: (Boolean) -> Unit
    ) {
        val updateData = hashMapOf<String, Any>(
            "description" to photo,
            title to title
        )
        photoRef.document(photoId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }
    }

    fun signOut() = Firebase.auth.signOut()
}

sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Loading<T> : Resources<T>()
    class Success<T>(data: T?) : Resources<T>(data = data)
    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)
}
