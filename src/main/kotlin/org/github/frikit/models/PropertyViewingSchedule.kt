package org.github.frikit.models

import java.time.Duration
import java.time.Instant

data class PropertyViewingSchedule(
    val startTime: Instant,
    val endTime: Instant,
    val slotDuration: Duration,
    val ignoreSlots: List<Pair<Instant, Instant>> = emptyList(),
)
