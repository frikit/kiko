package org.github.frikit.services

import org.github.frikit.BaseTestClass
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

internal class LandlordServiceTest : BaseTestClass() {

    @Inject
    lateinit var landlordService: LandlordService

    @BeforeEach
    fun setUp() {
        landLordRepo.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        landLordRepo.deleteAll()
    }

    @Test
    fun testCleanDBShouldReturnNoElements() {
        assertEquals(0, landlordService.findAll().size)
    }

    @Test
    fun testAddLandlordInDB() {
        val landLord = generateLandLord()
        landlordService.add(landLord)
        assertEquals(1, landlordService.findAll().size)
        assertEquals(landLord, landlordService.findAll().first())
    }

    @Test
    fun testAddlLandlordsInDB() {
        val landLord1 = generateLandLord()
        val landLord2 = generateLandLord()
        val list = listOf(landLord1, landLord2).sortedBy { it.id }
        landlordService.add(list)
        assertEquals(2, landlordService.findAll().size)
        assertEquals(list, landlordService.findAll().sortedBy { it.id })
    }
}
