package org.github.frikit.controllers

import io.micronaut.http.annotation.*
import org.github.frikit.models.BookTimeSlot
import org.github.frikit.models.PropertyViewingSchedule
import org.github.frikit.models.TimeSlot
import org.github.frikit.services.PropertyManagementService

@Controller("/calendar")
class CalendarController(
    private val propMgt: PropertyManagementService
) {

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
