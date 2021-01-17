package org.github.frikit.controllers

import org.github.frikit.BaseTestClass
import org.github.frikit.models.PropertyViewingSchedule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.inject.Inject

internal class CalendarControllerTest : BaseTestClass() {

    @Inject
    lateinit var controller: CalendarController

    private fun generateOneHourSlotWith20MinutesSlot(): PropertyViewingSchedule {
        val start = initSchedule(2020, 12, 1, 9, 9)
        val end = initSchedule(2020, 12, 1, 10, 9)
        val slot = Duration.of(20, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        return schedule
    }

    @BeforeEach
    fun setUp() {
        propertyCalendarDatabase.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        propertyCalendarDatabase.deleteAll()
    }

    @Test
    fun addCalendarForProperty() {
        val landLordID = "test1"
        val propertyID = "prop1"
        val schedule = generateOneHourSlotWith20MinutesSlot()

        controller.addCalendarForProperty(landLordID, propertyID, schedule)

        assertEquals(1, propertyCalendarDatabase.findAll().size, "In system should be only one property")
        assertEquals(
            propertyID,
            propertyCalendarDatabase.findAll().first().id,
            "In system should be only one property with id"
        )
        assertEquals(
            landLordID,
            propertyCalendarDatabase.findAll().first().ownerID,
            "In system should be only one property with owner id"
        )
        assertEquals(
            3,
            propertyCalendarDatabase.findAll().first().slots.size,
            "In system should be only one property with 3 slots"
        )
        assertEquals(
            3,
            propertyCalendarDatabase.findAll().first().slots.map { it.bookedByTenantID }.count { it == null },
            "In system should be only one property with 3 slots and all 3 slots should not be booked"
        )
    }
}
