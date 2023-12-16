package com.po.photoalbum.ui.model

import com.google.firebase.Timestamp

data class PhotoAlbumsDTO(
    val userId: String = "",
    val url : String ="",
    val timeStamp: Timestamp = Timestamp.now(),
    val documentId: String = ""
)
