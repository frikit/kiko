package org.github.frikit.database

import io.micronaut.context.annotation.Context
import org.github.frikit.models.Landlord
import java.util.concurrent.ConcurrentHashMap

@Context
class InMemoryLandlordDatabase : Database<Landlord> {

    private val db = ConcurrentHashMap<String, Landlord>()

    override fun save(t: Landlord): Landlord {
        if (db.contains(t.id)) throw RuntimeException("Landlord with ID=${t.id} already exist in DB!")
        db[t.id] = t
        return t
    }

    override fun saveAll(t: Collection<Landlord>): Collection<Landlord> {
        t.forEach { save(it) }
        return t
    }

    override fun findByID(id: String): Landlord {
        return db[id] ?: throw RuntimeException("Can't find landlord with ID=$id")
    }

    override fun findAll(): Collection<Landlord> {
        return db.values.toList()
    }

    override fun deleteAll() {
        db.clear()
    }

    override fun delete(id: String) {
        db.remove(id)
    }

}
