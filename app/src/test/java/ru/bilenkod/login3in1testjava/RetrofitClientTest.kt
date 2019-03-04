package ru.bilenkod.login3in1testjava

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.bilenkod.login3in1testjava.data.githubusersearch.client.RetrofitClient

class RetrofitClientTest {

    @Test
    fun instance_isSingleton() {
        val client1 = RetrofitClient.instance
        val client2 = RetrofitClient.instance

        assertEquals(client1, client2)
    }

    @Test
    fun gitHubApi_isSingleton() {
        val apiOne = RetrofitClient.gitHubApi
        val apiTwo = RetrofitClient.gitHubApi

        assertEquals(apiOne.hashCode(), apiTwo.hashCode())
    }

}