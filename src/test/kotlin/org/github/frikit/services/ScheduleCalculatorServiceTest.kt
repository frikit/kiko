package org.github.frikit.services

import org.github.frikit.BaseTestClass
import org.github.frikit.models.PropertyViewingSchedule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.inject.Inject

internal class ScheduleCalculatorServiceTest : BaseTestClass() {

    @Inject
    lateinit var scheduleCalculatorService: ScheduleCalculatorService

    @Test
    fun testCalculateSlotsInOneHourWith20MinSlot() {
        val start = initSchedule(2020, 12, 1, 9, 9)
        val end = initSchedule(2020, 12, 1, 10, 9)
        val slot = Duration.of(20, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        val res = scheduleCalculatorService.calculateSlots(schedule)

        Assertions.assertEquals(3, res.size)
    }

    @Test
    fun testCalculateSlotsInOneHourWith21MinSlot() {
        val start = initSchedule(2020, 12, 1, 9, 9)
        val end = initSchedule(2020, 12, 1, 10, 9)
        val slot = Duration.of(21, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        val res = scheduleCalculatorService.calculateSlots(schedule)

        Assertions.assertEquals(2, res.size)
    }

    @Test
    fun testCalculateSlotsButDurationIsExactOneSlotTobeAdded() {
        val start = initSchedule(2020, 12, 1, 9, 9)
        val end = initSchedule(2020, 12, 1, 10, 9)
        val slot = Duration.of(60, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        val res = scheduleCalculatorService.calculateSlots(schedule)

        Assertions.assertEquals(1, res.size)
    }

    @Test
    fun testCalculateSlotsButDurationIsBiggerThenOneSlotTobeAdded() {
        val start = initSchedule(2020, 12, 1, 9, 9)
        val end = initSchedule(2020, 12, 1, 10, 8)
        val slot = Duration.of(60, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        val res = scheduleCalculatorService.calculateSlots(schedule)

        Assertions.assertEquals(0, res.size)
    }
}
