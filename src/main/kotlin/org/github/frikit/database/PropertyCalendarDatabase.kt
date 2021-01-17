package org.github.frikit.database

import io.micronaut.context.annotation.Context
import org.github.frikit.models.Property
import java.util.concurrent.ConcurrentHashMap

@Context
class PropertyCalendarDatabase : Database<Property> {

    private val db = ConcurrentHashMap<String, Property>()

    override fun save(t: Property): Property {
        db[t.id] = t
        return t
    }

    override fun saveAll(t: Collection<Property>): Collection<Property> {
        t.forEach { save(it) }
        return t
    }

    override fun findByID(id: String): Property {
        return db[id] ?: throw RuntimeException("Can't find property with ID=$id")
    }

    override fun findAll(): Collection<Property> {
        return db.values.toList()
    }

    override fun deleteAll() {
        db.clear()
    }

    override fun delete(id: String) {
        db.remove(id)
    }

}
