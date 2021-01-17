package org.github.frikit.controllers

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import org.github.frikit.models.old.Landlord
import org.github.frikit.services.LandlordService

@Controller("/landlords")
class LandLordController(
    private val landlordService: LandlordService
) {

    @Get("/all", produces = [MediaType.APPLICATION_JSON])
    fun allLandlords(): Collection<Landlord> {
        return landlordService.findAll()
    }

    @Get("/landlord/{id}")
    fun getLandLord(@PathVariable id: String): Landlord {
        return landlordService.findByID(id)
    }

    @Put("/landlord")
    fun addLandLord(@Body ll: Landlord) {
        landlordService.add(ll)
    }

}
