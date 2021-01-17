package org.github.frikit.services

import io.micronaut.context.annotation.Context
import org.github.frikit.database.Database
import org.github.frikit.models.Property
import org.github.frikit.models.PropertyViewingSchedule

@Context
class PropertyManagementService(
    private val repository: Database<Property>,
    private val scheduleCalculatorService: ScheduleCalculatorService
) {

    fun findByID(propertyID: String): Property {
        return repository.findByID(propertyID)
    }

    fun createScheduleForProperty(propertyID: String, schedule: PropertyViewingSchedule) {
        val slots = scheduleCalculatorService.calculateSlots(schedule)
    }


}
