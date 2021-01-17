package org.github.frikit.services

import org.github.frikit.BaseTestClass
import org.github.frikit.models.PropertyViewingSchedule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject

internal class ScheduleCalculatorServiceTest : BaseTestClass() {

    @Inject
    lateinit var scheduleCalculatorService: ScheduleCalculatorService

    private fun initTime(year: Int, month: Int, date: Int, hour: Int = 0, minute: Int = 0): Instant {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"))
        calendar.set(year, month, date, hour, minute)

        return calendar.toInstant()
    }

    @Test
    fun testCalculateSlotsInOneHourWith20MinSlot() {
        val start = initTime(2020, 12, 1, 9, 9)
        val end = initTime(2020, 12, 1, 10, 9)
        val slot = Duration.of(20, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        val res = scheduleCalculatorService.calculateSlots(schedule)

        Assertions.assertEquals(3, res.size)
    }

    @Test
    fun testCalculateSlotsInOneHourWith21MinSlot() {
        val start = initTime(2020, 12, 1, 9, 9)
        val end = initTime(2020, 12, 1, 10, 9)
        val slot = Duration.of(21, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        val res = scheduleCalculatorService.calculateSlots(schedule)

        Assertions.assertEquals(2, res.size)
    }

    @Test
    fun testCalculateSlotsButDurationIsExactOneSlotTobeAdded() {
        val start = initTime(2020, 12, 1, 9, 9)
        val end = initTime(2020, 12, 1, 10, 9)
        val slot = Duration.of(60, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        val res = scheduleCalculatorService.calculateSlots(schedule)

        Assertions.assertEquals(1, res.size)
    }

    @Test
    fun testCalculateSlotsButDurationIsBiggerThenOneSlotTobeAdded() {
        val start = initTime(2020, 12, 1, 9, 9)
        val end = initTime(2020, 12, 1, 10, 8)
        val slot = Duration.of(60, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        val res = scheduleCalculatorService.calculateSlots(schedule)

        Assertions.assertEquals(0, res.size)
    }
}
