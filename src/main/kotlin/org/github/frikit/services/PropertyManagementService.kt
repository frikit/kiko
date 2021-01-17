package org.github.frikit.services

import io.micronaut.context.annotation.Context
import org.github.frikit.database.Database
import org.github.frikit.models.Landlord
import org.github.frikit.models.Property

@Context
class PropertyManagementService(
    private val repository: Database<Landlord>
) {

    fun findAll(): List<Property> {
        return repository.findAll().map { it.properties }.flatten()
    }

    fun findByID(landlordID: String): List<Property> {
        return repository.findByID(landlordID).properties
    }

    fun findByID(landlordID: String, propID: String): Property {
        return repository.findByID(landlordID).properties.find { it.id == propID }
            ?: throw RuntimeException("Unable to find prop ID=$propID")
    }

    fun add(landlordID: String, prop: Property) {
        add(landlordID, listOf(prop))
    }

    fun add(landlordID: String, prop: List<Property>) {
        val findLandlord = repository.findByID(landlordID)
        repository.delete(findLandlord.id)
        repository.save(findLandlord.copy(properties = prop))
    }

}
