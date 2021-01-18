package org.github.frikit.services

import org.github.frikit.BaseTestClass
import org.github.frikit.models.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.inject.Inject

internal class PropertyManagementServiceTest : BaseTestClass() {


    @Inject
    lateinit var service: PropertyManagementService

    @Inject
    lateinit var notificationService: NotificationService

    val landLordID = "test1"
    val propertyID = "prop1"
    val tenantID = "tenant1"

    private fun generateOneHourSlotWith20MinutesSlot(): List<PropertyViewingSchedule> {
        val start = initSchedule(2020, 12, 1, 9, 9)
        val end = initSchedule(2020, 12, 1, 10, 9)
        val slot = Duration.of(20, ChronoUnit.MINUTES)
        val schedule = PropertyViewingSchedule(start, end, slot, emptyList())

        return listOf(schedule)
    }

    private fun generateOneHourSlotWith20MinutesSlotForNext7Days(): List<PropertyViewingSchedule> {
        return (1..7).map { day ->
            val start = initSchedule(2020, 12, day, 10, 0)
            val end = initSchedule(2020, 12, day, 20, 0)
            val slot = Duration.of(20, ChronoUnit.MINUTES)
            PropertyViewingSchedule(start, end, slot, emptyList())
        }
    }

    private fun createScheduleForOneDay() {
        val schedule = generateOneHourSlotWith20MinutesSlot()

        service.createScheduleForProperty(landLordID, propertyID, schedule)
    }

    private fun createScheduleFor7Days() {
        val schedule = generateOneHourSlotWith20MinutesSlotForNext7Days()

        service.createScheduleForProperty(landLordID, propertyID, schedule)
    }

    @BeforeEach
    fun setUp() {
        propertyCalendarDatabase.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        propertyCalendarDatabase.deleteAll()
    }

    @Test
    fun testAddCalendarForPropertyForOneDay() {
        createScheduleForOneDay()

        assertEquals(1, propertyCalendarDatabase.findAll().size, "In system should be only one property")
        assertEquals(
            propertyID,
            propertyCalendarDatabase.findAll().first().id,
            "In system should be only one property with id"
        )
        assertEquals(
            landLordID,
            propertyCalendarDatabase.findAll().first().ownerID,
            "In system should be only one property with owner id"
        )
        assertEquals(
            3,
            propertyCalendarDatabase.findAll().first().slots.size,
            "In system should be only one property with 3 slots"
        )
        assertEquals(
            3,
            propertyCalendarDatabase.findAll().first().slots.map { it.bookedByTenantID }.count { it == null },
            "In system should be only one property with 3 slots and all 3 slots should not be booked"
        )
    }

    @Test
    fun testAddCalendarForPropertyForMultiDay() {
        createScheduleFor7Days()
        assertEquals(1, propertyCalendarDatabase.findAll().size, "In system should be only one property")
        assertEquals(
            propertyID,
            propertyCalendarDatabase.findAll().first().id,
            "In system should be only one property with id"
        )
        assertEquals(
            landLordID,
            propertyCalendarDatabase.findAll().first().ownerID,
            "In system should be only one property with owner id"
        )
        assertEquals(
            210,
            propertyCalendarDatabase.findAll().first().slots.size,
            "In system should be only one property with 231 slots for all days"
        )
        assertEquals(
            210,
            propertyCalendarDatabase.findAll().first().slots.map { it.bookedByTenantID }.count { it == null },
            "In system should be only one property with 231 slots for all days and none are booked"
        )
    }

    @Test
    fun testGetSlotsForProperty() {
        createScheduleForOneDay()
        val slots = service.getSlotsForProperty(propertyID)

        assertEquals(3, slots.size, "In system should be only one property")
        assertEquals(
            3,
            slots.map { it.bookedByTenantID }.count { it == null },
            "In system should be only one property with 3 slots and all 3 slots should not be booked"
        )
    }

    @Test
    fun testBookSlot() {
        createScheduleForOneDay()
        val slotToBook = service.getSlotsForProperty(propertyID).first()

        service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        val currentSlots = service.getSlotsForProperty(propertyID)

        assertEquals(3, currentSlots.size, "In system should be only one property with 3 slots")
        assertEquals(
            2,
            currentSlots.map { it.bookedByTenantID }.count { it == null },
            "In system should be only one property with 3 slots and 2 slots should not be booked"
        )
        assertEquals(
            1,
            currentSlots.map { it.bookedByTenantID }.count { it != null },
            "In system should be only one property with 3 slots and 1 slot should be booked"
        )

        //check status is waiting
        assertNotNull(currentSlots.find { it.status == SlotStatus.WAITING_CONFIRMATION })
        assertEquals(1, currentSlots.filter { it.status == SlotStatus.WAITING_CONFIRMATION }.size)
        assertEquals(
            SlotStatus.WAITING_CONFIRMATION,
            currentSlots.first { it.status == SlotStatus.WAITING_CONFIRMATION }.status
        )
    }

    @Test
    fun testBookSlotTwiceSameTenant() {
        createScheduleFor7Days()
        val slotToBook = service.getSlotsForProperty(propertyID).first()

        service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        assertThrows<RuntimeException>(
            "Should throw exception as slot is waiting confirmation"
        ) {
            service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        }
    }

    @Test
    fun testBookSlotTwiceDifferentTenant() {
        createScheduleFor7Days()
        val slotToBook = service.getSlotsForProperty(propertyID).first()

        service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        assertThrows<RuntimeException>(
            "Should throw exception as slot is waiting confirmation"
        ) {
            service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        }
    }

    @Test
    fun testBookSlotAcceptByLandlordResponseCannotBeBooked() {
        createScheduleForOneDay()
        val slotToBook = service.getSlotsForProperty(propertyID).first()

        service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        val currentSlots = service.getSlotsForProperty(propertyID)

        assertEquals(3, currentSlots.size, "In system should be only one property with 3 slots")
        assertEquals(
            2,
            currentSlots.map { it.bookedByTenantID }.count { it == null },
            "In system should be only one property with 3 slots and 2 slots should not be booked"
        )
        assertEquals(
            1,
            currentSlots.map { it.bookedByTenantID }.count { it != null },
            "In system should be only one property with 3 slots and 1 slot should be booked"
        )

        //check status is waiting
        assertNotNull(currentSlots.find { it.status == SlotStatus.WAITING_CONFIRMATION })
        assertEquals(1, currentSlots.filter { it.status == SlotStatus.WAITING_CONFIRMATION }.size)
        assertEquals(
            SlotStatus.WAITING_CONFIRMATION,
            currentSlots.first { it.status == SlotStatus.WAITING_CONFIRMATION }.status
        )

        //accept notification
        val notification = notificationService.notifications.entries.first().value
        val response =
            NotificationResponse(
                id = notification.id,
                propID = notification.propID,
                response = ResponseType.ACCEPT,
                from = UserType.LAND_LORD
            )

        notificationService.updateSlotFromNotificationResponse(response)

        assertThrows<RuntimeException>(
            "Should throw exception as slot is booked"
        ) {
            service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        }
    }

    @Test
    fun testBookSlotRejectedByLandlordResponseCannotBeBooked() {
        createScheduleForOneDay()
        val slotToBook = service.getSlotsForProperty(propertyID).first()

        service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        val currentSlots = service.getSlotsForProperty(propertyID)

        assertEquals(3, currentSlots.size, "In system should be only one property with 3 slots")
        assertEquals(
            2,
            currentSlots.map { it.bookedByTenantID }.count { it == null },
            "In system should be only one property with 3 slots and 2 slots should not be booked"
        )
        assertEquals(
            1,
            currentSlots.map { it.bookedByTenantID }.count { it != null },
            "In system should be only one property with 3 slots and 1 slot should be booked"
        )

        //check status is waiting
        assertNotNull(currentSlots.find { it.status == SlotStatus.WAITING_CONFIRMATION })
        assertEquals(1, currentSlots.filter { it.status == SlotStatus.WAITING_CONFIRMATION }.size)
        assertEquals(
            SlotStatus.WAITING_CONFIRMATION,
            currentSlots.first { it.status == SlotStatus.WAITING_CONFIRMATION }.status
        )

        //accept notification
        val notification = notificationService.notifications.entries.first().value
        val response =
            NotificationResponse(
                id = notification.id,
                propID = notification.propID,
                response = ResponseType.REJECT,
                from = UserType.LAND_LORD
            )

        notificationService.updateSlotFromNotificationResponse(response)

        assertThrows<RuntimeException>(
            "Should throw exception as slot is booked"
        ) {
            service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        }
    }

    @Test
    fun testBookSlotRejectedByTenantResponseCanBeBookedAgain() {
        createScheduleForOneDay()
        val slotToBook = service.getSlotsForProperty(propertyID).first()

        service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        val currentSlots = service.getSlotsForProperty(propertyID)

        assertEquals(3, currentSlots.size, "In system should be only one property with 3 slots")
        assertEquals(
            2,
            currentSlots.map { it.bookedByTenantID }.count { it == null },
            "In system should be only one property with 3 slots and 2 slots should not be booked"
        )
        assertEquals(
            1,
            currentSlots.map { it.bookedByTenantID }.count { it != null },
            "In system should be only one property with 3 slots and 1 slot should be booked"
        )

        //check status is waiting
        assertNotNull(currentSlots.find { it.status == SlotStatus.WAITING_CONFIRMATION })
        assertEquals(1, currentSlots.filter { it.status == SlotStatus.WAITING_CONFIRMATION }.size)
        assertEquals(
            SlotStatus.WAITING_CONFIRMATION,
            currentSlots.first { it.status == SlotStatus.WAITING_CONFIRMATION }.status
        )

        //accept notification
        val notification = notificationService.notifications.entries.first().value
        val response =
            NotificationResponse(
                id = notification.id,
                propID = notification.propID,
                response = ResponseType.REJECT,
                from = UserType.TENANT
            )

        notificationService.updateSlotFromNotificationResponse(response)

        assertDoesNotThrow(
            "Should not throw exception as slot is booked"
        ) {
            service.bookSlot(propertyID, tenantID, slotToBook.start, slotToBook.end)
        }
    }

}
