package org.github.frikit.models

import java.time.Instant

data class TimeSlot(
    var bookedByTenantID: String? = null,
    val start: Instant,
    val end: Instant,
    val status: SlotStatus = SlotStatus.NOT_BOOKED
) {
    fun canBeBooked(): Boolean {
        return status == SlotStatus.NOT_BOOKED
    }
}

fun List<Pair<Instant, Instant>>.toTimeSlots(): List<TimeSlot> {
    return this.map { TimeSlot(start = it.first, end = it.second) }
}
