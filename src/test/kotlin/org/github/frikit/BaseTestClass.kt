package org.github.frikit

import io.micronaut.http.HttpRequest
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.github.frikit.database.InMemoryLandlordDatabase
import org.github.frikit.models.Landlord
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@MicronautTest
abstract class BaseTestClass {

    private val log = LoggerFactory.getLogger(BaseTestClass::class.java)

    @Inject
    lateinit var landLordRepo: InMemoryLandlordDatabase

    @Inject
    lateinit var httpHostResolver: HttpHostResolver

    fun generateLandLord(): Landlord {
        return Landlord(
            id = UUID.randomUUID().toString(),
            name = UUID.randomUUID().toString().take(4),
            properties = emptyList()
        )
    }

    fun buildURL(url: String): String {
        val req = HttpRequest.GET<Void>(url)
        val hostURL = httpHostResolver.resolve(req)
        val res = if (hostURL.endsWith("/")) hostURL.substring(0, hostURL.length - 1) + url else hostURL + url
        log.info("Build URL => [$res]")
        return res
    }

}
