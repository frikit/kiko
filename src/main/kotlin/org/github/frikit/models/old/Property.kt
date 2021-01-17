package org.github.frikit.models.old

import java.time.Instant

data class Property(
    val id: String,
    val type: PropertyType,
    val street: String,
    val city: String,
    val country: String,
    val postcode: String,

    val availableSpots: List<Pair<Instant, Instant>>
)
