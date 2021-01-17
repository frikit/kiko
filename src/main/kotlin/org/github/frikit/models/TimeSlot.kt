package org.github.frikit.models

import java.time.Instant

data class TimeSlot(
    val start: Instant,
    val end: Instant,
)
