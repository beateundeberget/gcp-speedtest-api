package com.speedtest.api

import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.gson.Gson
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController
@RequestMapping("/speedtest")
class SpeedTestResource(val pubSub: PubSubTemplate) {

    data class TestResult(
        val user: String,
        val device: String,
        val timestamp: Timestamp,
        val data: Results,
    )

    data class Results(
        val speeds: Speeds,
        val client: Client,
        val server: Server
    )

    data class Speeds(
        val download: Number,
        val upload: Number
    )

    data class Client(
        val ip: String?,
        val lat: Number?,
        val lon: Number?,
        val isp: String?,
        val country: String?
    )

    data class Server(
        val host: String?,
        val lat: Number?,
        val lon: Number?,
        val country: String?,
        val distance: Number?,
        val ping: Number?,
        val id: String?
    )

    val gson: Gson = Gson()

    @PostMapping
    fun publishTestResult(@RequestBody testResult: TestResult) {
        this.pubSub.publish("speedtest", gson.toJson(testResult))
        println(testResult.user)
    }
}
