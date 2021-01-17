package org.github.frikit.models

data class Property(
    val id: String,
    val ownerID: String,
    val slots: List<TimeSlot>
)
