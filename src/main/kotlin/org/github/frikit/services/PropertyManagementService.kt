package org.github.frikit.services

import io.micronaut.context.annotation.Context
import org.github.frikit.database.Database
import org.github.frikit.models.*
import java.time.Instant
import java.util.*

@Context
class PropertyManagementService(
    private val repository: Database<Property>,
    private val scheduleCalculatorService: ScheduleCalculatorService,
    private val notificationService: NotificationService,
) {

    fun createScheduleForProperty(landlordID: String, propertyID: String, schedule: List<PropertyViewingSchedule>) {
        val slots = schedule.flatMap { scheduleCalculatorService.calculateSlots(it) }

        repository.save(Property(id = propertyID, ownerID = landlordID, slots = slots.toTimeSlots()))
    }

    fun getSlotsForProperty(propertyID: String): List<TimeSlot> {
        return repository.findByID(propertyID).slots
    }

    fun bookSlot(propertyID: String, tenantID: String, start: Instant, end: Instant) {
        val property = repository.findByID(propertyID)

        var booked = false
        val newSlots = property.slots.map { slot ->
            if (slot.start == start && slot.end == end) {
                if (!slot.canBeBooked())
                    throw RuntimeException("Slot you are trying to book cannot be booked at this moment!")

                booked = true
                notificationService.sendNotification(
                    Notification(
                        id = UUID.randomUUID().toString(),
                        propID = propertyID,
                        userEmail = tenantID,
                        landlordEmail = property.ownerID,
                        where = "url",
                        text = "Notification ask for confirmation book viewing",
                        bookTimeSlot = BookTimeSlot(start = start, end = end)
                    )
                )
                slot.copy(bookedByTenantID = tenantID, status = SlotStatus.WAITING_CONFIRMATION)
            } else {
                slot
            }
        }
        if (!booked) throw RuntimeException("Can't find such slot in system!")

        repository.save(property.copy(slots = newSlots))
    }
}
