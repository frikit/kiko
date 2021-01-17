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

    @Put("/calendar/{propertyID}")
    fun addCalendarForProperty(
        @PathVariable propertyID: String,
        @Body schedule: PropertyViewingSchedule
    ): String {
        propMgt.createScheduleForProperty(propertyID, schedule)
        return "Hello world"
    }
}
