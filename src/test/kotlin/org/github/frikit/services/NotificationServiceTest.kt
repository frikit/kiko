package org.github.frikit.services

import org.github.frikit.BaseTestClass
import org.github.frikit.models.*
import org.junit.jupiter.api.*
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.inject.Inject

internal class NotificationServiceTest : BaseTestClass() {

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var propertyManagementService: PropertyManagementService

    private fun generateOneHourSlotWith20MinutesSlot(): List<PropertyViewingSchedule> {
        val start = initSchedule(2020, 12, 1, 9, 9)
        val end = initSchedule(2020, 12, 1, 10, 9)
        val slot = Duration.of(20, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        return listOf(schedule)
    }

    val landLordID = "test1"
    val propertyID = "prop1"

    @BeforeEach
    fun setUp() {
        notificationService.notifications.clear()

        val schedule = generateOneHourSlotWith20MinutesSlot()
        propertyManagementService.createScheduleForProperty(landLordID, propertyID, schedule)
    }

    @AfterEach
    fun tearDown() {
        notificationService.notifications.clear()
        propertyCalendarDatabase.deleteAll()
    }

    @Test
    fun testSendNotification() {
        val slotToBook = propertyManagementService.getSlotsForProperty(propertyID).first()
        val notification = Notification(
            id = "id1",
            propID = propertyID,
            where = "url",
            text = "test notification",
            bookTimeSlot = BookTimeSlot(slotToBook.start, slotToBook.end)
        )

        notificationService.sendNotification(notification)

        Assertions.assertEquals(1, notificationService.notifications.size)
    }

    @Test
    fun testSendAcceptResponseToNotification() {
        val slotToBook = propertyManagementService.getSlotsForProperty(propertyID).first()
        val notification = Notification(
            id = "id1",
            propID = propertyID,
            where = "url",
            text = "test notification",
            bookTimeSlot = BookTimeSlot(slotToBook.start, slotToBook.end)
        )

        notificationService.sendNotification(notification)

        Assertions.assertEquals(1, notificationService.notifications.size)

        //send response
        val response =
            NotificationResponse(id = notification.id, propID = notification.propID, response = ResponseType.ACCEPT)

        notificationService.updateSlotFromNotificationResponse(response)

        Assertions.assertEquals(0, notificationService.notifications.size)
    }

    @Test
    fun testSendRejectResponseToNotification() {
        val slotToBook = propertyManagementService.getSlotsForProperty(propertyID).first()
        val notification = Notification(
            id = "id1",
            propID = propertyID,
            where = "url",
            text = "test notification",
            bookTimeSlot = BookTimeSlot(slotToBook.start, slotToBook.end)
        )

        notificationService.sendNotification(notification)

        Assertions.assertEquals(1, notificationService.notifications.size)

        //send response
        val response =
            NotificationResponse(id = notification.id, propID = notification.propID, response = ResponseType.REJECT)

        notificationService.updateSlotFromNotificationResponse(response)

        Assertions.assertEquals(0, notificationService.notifications.size)
    }

    @Test
    fun testSendAcceptResponseTwoTimesToNotification() {
        val slotToBook = propertyManagementService.getSlotsForProperty(propertyID).first()
        val notification = Notification(
            id = "id1",
            propID = propertyID,
            where = "url",
            text = "test notification",
            bookTimeSlot = BookTimeSlot(slotToBook.start, slotToBook.end)
        )

        notificationService.sendNotification(notification)

        Assertions.assertEquals(1, notificationService.notifications.size)

        //send response
        val response =
            NotificationResponse(id = notification.id, propID = notification.propID, response = ResponseType.ACCEPT)

        notificationService.updateSlotFromNotificationResponse(response)
        Assertions.assertEquals(0, notificationService.notifications.size)

        assertThrows<RuntimeException>(
            "Should throw exception as notification doesnt exist anymore and cant be processed again"
        ) {
            notificationService.updateSlotFromNotificationResponse(response)
        }
    }

    @Test
    fun testSendRejectResponseTwoTimesToNotification() {
        val slotToBook = propertyManagementService.getSlotsForProperty(propertyID).first()
        val notification = Notification(
            id = "id1",
            propID = propertyID,
            where = "url",
            text = "test notification",
            bookTimeSlot = BookTimeSlot(slotToBook.start, slotToBook.end)
        )

        notificationService.sendNotification(notification)

        Assertions.assertEquals(1, notificationService.notifications.size)

        //send response
        val response =
            NotificationResponse(id = notification.id, propID = notification.propID, response = ResponseType.REJECT)

        notificationService.updateSlotFromNotificationResponse(response)
        Assertions.assertEquals(0, notificationService.notifications.size)

        assertThrows<RuntimeException>(
            "Should throw exception as notification doesnt exist anymore and cant be processed again"
        ) {
            notificationService.updateSlotFromNotificationResponse(response)
        }
    }

}
