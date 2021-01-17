package org.github.frikit.services

import io.micronaut.context.annotation.Context
import org.github.frikit.models.PropertyViewingSchedule
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@Context
class ScheduleCalculatorService {

    //TODO add it later
    //TODO extract into props
    val breakBetweenViewings = Duration.of(0, ChronoUnit.MINUTES).toMillis()

    /**
     * TODO If last time slot is < then slotDuration but it is > 5 minute should I add it???
     */
    fun calculateSlots(schedule: PropertyViewingSchedule): List<Pair<Instant, Instant>> {
        val slots = mutableListOf<Pair<Instant, Instant>>()

        val incBy = schedule.slotDuration.toMinutes()
        var curr = schedule.startTime
        var stop = false

        while (!stop) {
            val from = curr

            val potential = curr
                .plus(incBy, ChronoUnit.MINUTES)
                .plus(breakBetweenViewings, ChronoUnit.MINUTES)

            if (potential > schedule.endTime) {
                stop = true
            } else {
                slots.add(Pair(from, potential))
                curr = potential
            }
        }

        return slots
    }


}
