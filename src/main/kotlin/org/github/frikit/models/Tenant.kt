package org.github.frikit.models

import java.time.Instant

data class Tenant(
    val id: String,
    val name: String,
    val bookedSpots: List<Pair<Instant, Instant>>
)
