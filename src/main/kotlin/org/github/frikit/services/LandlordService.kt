package org.github.frikit.services

import io.micronaut.context.annotation.Context
import org.github.frikit.database.Database
import org.github.frikit.models.Landlord

@Context
class LandlordService(
    private val repository: Database<Landlord>
) {

    fun findAll(): Collection<Landlord> {
        return repository.findAll()
    }

    fun findByID(id: String): Landlord {
        return repository.findByID(id)
    }

    fun add(landlord: Landlord) {
        add(listOf(landlord))
    }

    fun add(landlord: List<Landlord>) {
        repository.saveAll(landlord)
    }

}
