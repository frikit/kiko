package org.github.frikit.models

data class NotificationResponse(
    val id: String,
    val propID: String,
    val response: ResponseType,
    val from: UserType
)
