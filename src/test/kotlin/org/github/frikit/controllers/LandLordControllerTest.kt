//package org.github.frikit.controllers
//
//import io.micronaut.core.type.Argument
//import io.micronaut.http.HttpRequest
//import io.micronaut.http.HttpStatus
//import io.micronaut.http.client.annotation.Client
//import io.micronaut.http.client.netty.DefaultHttpClient
//import org.github.frikit.BaseTestClass
//import org.github.frikit.models.old.Landlord
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertNotNull
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import javax.inject.Inject
//
//internal class LandLordControllerTest : BaseTestClass() {
//
//    @Inject
//    @Client("/")
//    lateinit var http: DefaultHttpClient
//
//    @BeforeEach
//    fun setUp() {
//        landLordRepo.deleteAll()
//    }
//
//    @AfterEach
//    fun tearDown() {
//        landLordRepo.deleteAll()
//    }
//
//    @Test
//    fun testAddLandLord() {
//        val landLord = generateLandLord()
//        val url = buildURL("/landlords/landlord")
//        val req = HttpRequest.PUT(url, landLord)
//        val resp = http.toBlocking().exchange(req, Argument.VOID)
//
//        assertEquals(HttpStatus.OK.code, resp.status.code)
//        assertEquals(1, landLordRepo.findAll().size)
//    }
//
//    @Test
//    fun testGetLandlordByID() {
//        val landLord = generateLandLord()
//        landLordRepo.save(landLord)
//
//        val url = buildURL("/landlords/landlord/${landLord.id}")
//        val req = HttpRequest.GET<Landlord>(url)
//        val resp = http.toBlocking().exchange(req, Argument.of(Landlord::class.java))
//
//        assertEquals(HttpStatus.OK.code, resp.status.code)
//        assertNotNull(resp.body())
//        assertEquals(landLord, resp.body())
//    }
//}
