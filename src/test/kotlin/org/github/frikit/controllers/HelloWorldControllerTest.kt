package org.github.frikit.controllers

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.github.frikit.BaseTestClass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import javax.inject.Inject

@MicronautTest
internal class HelloWorldControllerTest : BaseTestClass() {

    @Inject
    lateinit var helloWorldController: HelloWorldController

    @Test
    fun helloWorld() {
        Assertions.assertEquals("Hello world", helloWorldController.helloWorld())
    }
}
