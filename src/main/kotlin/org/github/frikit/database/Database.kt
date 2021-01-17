package org.github.frikit.database

interface Database<T> {

    fun save(t: T): T

    fun saveAll(t: Collection<T>): Collection<T>

    fun findByID(id: String): T

    fun findAll(): Collection<T>

    fun deleteAll()
}
