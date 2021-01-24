package org.github.frikit.controllers

import io.micronaut.http.annotation.*
import org.github.frikit.models.BookTimeSlot
import org.github.frikit.models.PropertyViewingSchedule
import org.github.frikit.models.TimeSlot
import org.github.frikit.services.PropertyManagementService
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*

@Controller("/calendar")
class CalendarController(
    private val propMgt: PropertyManagementService
) {

    /**
     * EXAMPLES
     */
    @Get("/example/schedule")
    fun getExampleObject(): List<PropertyViewingSchedule> {
        //start
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"))
        calendar.set(2020, 1, 1, 1, 1)
        val start = calendar.toInstant()

        //end
        val calendar2 = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"))
        calendar2.set(2020, 1, 1, 5, 1)
        val end = calendar2.toInstant()

        val slot = Duration.of(20, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        return listOf(schedule)
    }


    @Get("/example/booktimeslot")
    fun getExampleObject2(): BookTimeSlot {
        //start
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"))
        calendar.set(2020, 1, 1, 1, 1)
        val start = calendar.toInstant()

        //end
        val calendar2 = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"))
        calendar2.set(2020, 1, 1, 1, 21)
        val end = calendar2.toInstant()

        return BookTimeSlot(start, end)
    }

    /**
     * END
     */

    @Get("/{propertyID}")
    fun getCalendarForProperty(
        @PathVariable propertyID: String
    ): List<TimeSlot> {
        return propMgt.getSlotsForProperty(propertyID)
    }

    /**
     * TODO If try to add for the same property schedule should check if it overlap if existing one and merge or override?
     */
    @Put("/{landlordID}/{propertyID}")
    fun addCalendarForProperty(
        @PathVariable landlordID: String,
        @PathVariable propertyID: String,
        @Body schedule: List<PropertyViewingSchedule>
    ) {
        propMgt.createScheduleForProperty(landlordID, propertyID, schedule)
    }

    @Put("/book/{propertyID}/{tenantID}")
    fun bookSlotForView(
        @PathVariable propertyID: String,
        @PathVariable tenantID: String,
        @Body bookTimeSlot: BookTimeSlot
    ) {
        propMgt.bookSlot(propertyID, tenantID, bookTimeSlot.start, bookTimeSlot.end)
    }
}
