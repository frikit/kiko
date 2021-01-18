package org.github.frikit.services

import io.micronaut.context.annotation.Context
import org.github.frikit.database.Database
import org.github.frikit.models.*
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

@Context
class NotificationService(
    private val repository: Database<Property>
) {

    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    val notifications = ConcurrentHashMap<String, Notification>()

    private fun sendNotificationService(notification: Notification) {
        log.info("Send notification $notification")
        Thread.sleep(300)//simulate
    }

    private fun sendEmail(subject: String, body: String, to: String) {
        log.info("Send email $subject AND $body AND $to")
        Thread.sleep(300)//simulate
    }

    //TODO make it async
    fun sendNotification(notification: Notification) {
        sendNotificationService(notification)
        notifications[notification.id] = notification
    }

    fun updateSlotFromNotificationResponse(notification: NotificationResponse) {
        fun ResponseType.getSLotStatus(): SlotStatus {
            return when (this) {
                ResponseType.ACCEPT -> SlotStatus.BOOKED
                ResponseType.REJECT -> SlotStatus.REJECTED_BY_LANDLORD
            }
        }

        val property = repository.findByID(notification.propID)
        val notif = notifications[notification.id] ?: throw RuntimeException("No such notification in the system!")
        when {
            tenantRejectedBooking(notification) -> handleTenantRejectedBooking(property, notif, notification)
            tenantAcceptedBooking(notification) -> return // no implementation needed
            //landlord
            else -> handleLandlordNotification(
                property,
                notif,
                notification.response.getSLotStatus(),
                notification
            )
        }
    }

    private fun handleLandlordNotification(
        property: Property,
        notif: Notification,
        status: SlotStatus,
        notification: NotificationResponse
    ) {
        val newSlots = getNewSlots(property, notif, status)
        repository.save(property.copy(slots = newSlots))
        sendEmail(
            "Property view: ${notification.response}",
            "Please be on time for view property!",
            notif.userEmail
        )
        notifications.remove(notification.id)
    }

    private fun handleTenantRejectedBooking(
        property: Property,
        notif: Notification,
        notification: NotificationResponse
    ) {
        val status = SlotStatus.NOT_BOOKED
        val newSlots = getNewSlots(property, notif, status, null)
        repository.save(property.copy(slots = newSlots))
        sendEmail("Property view: ${notification.response}", "User cancel viewing!", notif.landlordEmail)
        notifications.remove(notification.id)
    }

    private fun tenantRejectedBooking(notification: NotificationResponse) =
        notification.from == UserType.TENANT && notification.response == ResponseType.REJECT

    private fun tenantAcceptedBooking(notification: NotificationResponse) =
        notification.from == UserType.TENANT && notification.response == ResponseType.ACCEPT

    private fun getNewSlots(
        property: Property,
        notif: Notification,
        status: SlotStatus,
        bookedByTenantID: String?
    ): List<TimeSlot> {
        val newSlots = property.slots.map {
            if (it.start == notif.bookTimeSlot.start && it.end == notif.bookTimeSlot.end && it.status == SlotStatus.WAITING_CONFIRMATION)
                it.copy(bookedByTenantID = bookedByTenantID, status = status)
            else
                it
        }
        return newSlots
    }

    private fun getNewSlots(
        property: Property,
        notif: Notification,
        status: SlotStatus
    ): List<TimeSlot> {
        val newSlots = property.slots.map {
            if (it.start == notif.bookTimeSlot.start && it.end == notif.bookTimeSlot.end && it.status == SlotStatus.WAITING_CONFIRMATION)
                it.copy(status = status)
            else
                it
        }
        return newSlots
    }
}
