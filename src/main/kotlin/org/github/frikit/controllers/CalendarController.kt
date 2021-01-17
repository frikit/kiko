package org.github.frikit.controllers

import io.micronaut.http.annotation.*
import org.github.frikit.models.PropertyViewingSchedule
import org.github.frikit.services.PropertyManagementService

@Controller("/calendar")
class CalendarController(
    private val propMgt: PropertyManagementService
) {

    @Get("/calendar/{propertyID}")
    fun getCalendarForProperty(
        @PathVariable propertyID: String
    ): String {
        return "Hello world"
    }

    /**
     * TODO If try to add for the same property schedule should check if it overlap if existing one and merge or override?
     */
    @Put("/calendar/{landlordID}/{propertyID}")
    fun addCalendarForProperty(
        @PathVariable landlordID: String,
        @PathVariable propertyID: String,
        @Body schedule: PropertyViewingSchedule
    ) {
        propMgt.createScheduleForProperty(landlordID, propertyID, schedule)
    }
}
