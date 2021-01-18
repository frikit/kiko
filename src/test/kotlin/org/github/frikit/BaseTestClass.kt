package org.github.frikit

import io.micronaut.http.HttpRequest
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.github.frikit.database.PropertyCalendarDatabase
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import javax.inject.Inject

@MicronautTest(startApplication = true)
abstract class BaseTestClass {

    private val log = LoggerFactory.getLogger(BaseTestClass::class.java)

    @Inject
    lateinit var propertyCalendarDatabase: PropertyCalendarDatabase


    @Inject
    lateinit var httpHostResolver: HttpHostResolver

    fun buildURL(url: String): String {
        val req = HttpRequest.GET<Void>(url)
        val hostURL = httpHostResolver.resolve(req)
        val res = if (hostURL.endsWith("/")) hostURL.substring(0, hostURL.length - 1) + url else hostURL + url
        log.info("Build URL => [$res]")
        return res
    }

    fun initSchedule(year: Int, month: Int, date: Int, hour: Int = 0, minute: Int = 0): Instant {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"))
        calendar.set(year, month, date, hour, minute)

        return calendar.toInstant()
    }


}
