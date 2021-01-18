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
        val property = repository.findByID(notification.propID)
        val notif = notifications[notification.id] ?: throw RuntimeException("No such notification in the system!")

        if (notification.from == UserType.TENANT && notification.response == ResponseType.ACCEPT) {
            //ignore
        } else if (notification.from == UserType.TENANT && notification.response == ResponseType.REJECT) {
            val status = SlotStatus.NOT_BOOKED

            val newSlots = property.slots.map {
                if (it.start == notif.bookTimeSlot.start && it.end == notif.bookTimeSlot.end && it.status == SlotStatus.WAITING_CONFIRMATION)
                    it.copy(status = status)
                else
                    it
            }

            repository.save(property.copy(slots = newSlots))

            sendEmail("Property view: ${notification.response}", "User cancel viewing!", notif.landlordEmail)
            notifications.remove(notification.id)
        } else {
            //landlord response
            val status = if (notification.response == ResponseType.ACCEPT) {
                SlotStatus.BOOKED
            } else {
                SlotStatus.REJECTED_BY_LANDLORD
            }

            val newSlots = property.slots.map {
                if (it.start == notif.bookTimeSlot.start && it.end == notif.bookTimeSlot.end && it.status == SlotStatus.WAITING_CONFIRMATION)
                    it.copy(status = status)
                else
                    it
            }

            repository.save(property.copy(slots = newSlots))

            sendEmail(
                "Property view: ${notification.response}",
                "Please be on time for view property!",
                notif.userEmail
            )
            notifications.remove(notification.id)
        }
    }
}
