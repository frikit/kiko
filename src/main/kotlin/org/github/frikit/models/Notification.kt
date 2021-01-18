package org.github.frikit.models

data class Notification(
    val id: String,
    val propID: String,
    val where: String,
    val text: String,
    val bookTimeSlot: BookTimeSlot
)
