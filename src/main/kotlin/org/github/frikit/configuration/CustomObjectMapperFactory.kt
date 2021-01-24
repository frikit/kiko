package org.github.frikit.configuration

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.jackson.JacksonConfiguration
import io.micronaut.jackson.ObjectMapperFactory
import javax.inject.Singleton


@Factory
@Replaces(ObjectMapperFactory::class)
class CustomObjectMapperFactory : ObjectMapperFactory() {
    @Singleton
    @Replaces(ObjectMapper::class)
    override fun objectMapper(
        jacksonConfiguration: JacksonConfiguration?,
        jsonFactory: JsonFactory?
    ): ObjectMapper {
        val mapper = super.objectMapper(jacksonConfiguration, jsonFactory)
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        return mapper
    }
}
