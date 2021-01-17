package org.github.frikit.models

data class Landlord(
    val id: String,
    val name: String,
    val properties: List<Property> = emptyList()
)
