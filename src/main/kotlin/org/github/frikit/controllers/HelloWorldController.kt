package org.github.frikit.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/hello")
class HelloWorldController {

    @Get
    fun helloWorld(): String {
        return "Hello world"
    }
}
