package org.github.frikit.controllers

import io.micronaut.http.annotation.*
import org.github.frikit.models.Property
import org.github.frikit.services.PropertyManagementService

@Controller("/properties")
class PropertyController(
    private val propMgt: PropertyManagementService
) {

    @Get("/property/{landlordID}")
    fun getLandlordProperties(
        @PathVariable landlordID: String
    ): List<Property> {
        return propMgt.findByID(landlordID)
    }

    @Get("/property/{landlordID}/{id}")
    fun getProperty(
        @PathVariable landlordID: String,
        @PathVariable id: String
    ): Property {
        return propMgt.findByID(landlordID, id)
    }

    @Put("/property/{landlordID}")
    fun addProperty(
        @PathVariable landlordID: String,
        @Body prop: Property
    ) {
        propMgt.add(landlordID, prop)
    }

    @Put("/property/{landlordID}")
    fun addProperty(
        @PathVariable landlordID: String,
        @Body prop: List<Property>
    ) {
        propMgt.add(landlordID, prop)
    }

}
