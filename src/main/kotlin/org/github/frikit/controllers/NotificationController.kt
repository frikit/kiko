package org.github.frikit.controllers

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Put
import org.github.frikit.models.NotificationResponse
import org.github.frikit.services.NotificationService

@Controller("/notification")
class NotificationController(
    private val notificationService: NotificationService
) {

    @Put("landlord-response")
    fun getCalendarForProperty(
        @Body response: NotificationResponse
    ) {
        return notificationService.updateSlotFromNotificationResponse(response)
    }

}
