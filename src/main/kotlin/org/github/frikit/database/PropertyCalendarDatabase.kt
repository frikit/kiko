package org.github.frikit.database

import io.micronaut.context.annotation.Context
import org.github.frikit.models.Property
import org.github.frikit.models.old.Landlord
import java.util.concurrent.ConcurrentHashMap

@Context
class PropertyCalendarDatabase : Database<Property> {

    private val db = ConcurrentHashMap<String, Landlord>()
    override fun save(t: Property): Property {
        TODO("Not yet implemented")
    }

    override fun saveAll(t: Collection<Property>): Collection<Property> {
        TODO("Not yet implemented")
    }

    override fun findByID(id: String): Property {
        TODO("Not yet implemented")
    }

    override fun findAll(): Collection<Property> {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun delete(id: String) {
        TODO("Not yet implemented")
    }


}
