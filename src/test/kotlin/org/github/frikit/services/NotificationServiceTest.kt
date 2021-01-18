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

    private fun generateNotification(slotToBook: TimeSlot): Notification {
        val notification = Notification(
            id = "id1",
            propID = propertyID,
            userEmail = "test@test.com",
            landlordEmail = "landlord@test.com",
            where = "url",
            text = "test notification",
            bookTimeSlot = BookTimeSlot(slotToBook.start, slotToBook.end)
        )

        return notification
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
        sendNotification()

        Assertions.assertEquals(1, notificationService.notifications.size)
    }

    @Test
    fun testSendAcceptResponseToNotification() {
        val notification = sendNotification()

        Assertions.assertEquals(1, notificationService.notifications.size)

        //send response
        updateSlotSendNotification(notification, ResponseType.ACCEPT, UserType.LAND_LORD)

        Assertions.assertEquals(0, notificationService.notifications.size)
    }

    @Test
    fun testSendRejectResponseToNotification() {
        val notification = sendNotification()

        Assertions.assertEquals(1, notificationService.notifications.size)

        //send response
        updateSlotSendNotification(notification, ResponseType.REJECT, UserType.LAND_LORD)

        Assertions.assertEquals(0, notificationService.notifications.size)
    }

    private fun updateSlotSendNotification(
        notification: Notification,
        response: ResponseType,
        from: UserType
    ): NotificationResponse {
        val response = NotificationResponse(
            id = notification.id,
            propID = notification.propID,
            response = response,
            from = from
        )

        notificationService.updateSlotFromNotificationResponse(response)

        return response
    }

    @Test
    fun testSendAcceptResponseTwoTimesToNotification() {
        val notification = sendNotification()

        Assertions.assertEquals(1, notificationService.notifications.size)

        //send response
        val response = updateSlotSendNotification(notification, ResponseType.ACCEPT, UserType.LAND_LORD)

        Assertions.assertEquals(0, notificationService.notifications.size)

        assertThrows<RuntimeException>(
            "Should throw exception as notification doesnt exist anymore and cant be processed again"
        ) {
            notificationService.updateSlotFromNotificationResponse(response)
        }
    }

    @Test
    fun testSendRejectResponseTwoTimesToNotification() {
        val notification = sendNotification()

        Assertions.assertEquals(1, notificationService.notifications.size)

        //send response
        val response = updateSlotSendNotification(notification, ResponseType.REJECT, UserType.LAND_LORD)

        Assertions.assertEquals(0, notificationService.notifications.size)

        assertThrows<RuntimeException>(
            "Should throw exception as notification doesnt exist anymore and cant be processed again"
        ) {
            notificationService.updateSlotFromNotificationResponse(response)
        }
    }

    private fun sendNotification(): Notification {
        val slotToBook = propertyManagementService.getSlotsForProperty(propertyID).first()
        val notification = generateNotification(slotToBook)

        notificationService.sendNotification(notification)
        return notification
    }

}
