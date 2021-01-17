package org.github.frikit.services

import io.micronaut.context.annotation.Context
import org.github.frikit.database.Database
import org.github.frikit.models.Property
import org.github.frikit.models.PropertyViewingSchedule
import org.github.frikit.models.toTimeSlots

@Context
class PropertyManagementService(
    private val repository: Database<Property>,
    private val scheduleCalculatorService: ScheduleCalculatorService
) {

    fun findByID(propertyID: String): Property {
        return repository.findByID(propertyID)
    }

    fun createScheduleForProperty(landlordID: String, propertyID: String, schedule: PropertyViewingSchedule) {
        val slots = scheduleCalculatorService.calculateSlots(schedule)

        repository.save(Property(id = propertyID, ownerID = landlordID, slots = slots.toTimeSlots()))
    }
}
