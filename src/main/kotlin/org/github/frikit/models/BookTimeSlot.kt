package org.github.frikit.models

import java.time.Instant

data class BookTimeSlot(
    val start: Instant,
    val end: Instant,
)
