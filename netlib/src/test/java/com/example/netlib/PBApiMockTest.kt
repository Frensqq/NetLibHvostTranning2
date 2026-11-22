package com.example.netlib

import com.example.netlib.data.remote.PBApi
import com.example.netlib.data.remote.PBApiService
import com.example.netlib.data.repository.AppointmentRepositoryImpl
import com.example.netlib.data.repository.AuthRepositoryImpl
import com.example.netlib.domain.models.AuthRequest
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.Slots
import com.example.netlib.domain.models.UpdateAppointment
import com.example.netlib.domain.network.Network
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.collections.containsKey
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PBApiMockTest {



    private fun clientCreate(
        response: String,
        status: HttpStatusCode = HttpStatusCode.OK

    ): HttpClient =
        PBApiService("http://10.0.2.2/api/", { null }).create(MockEngine {
            respond(response, status, headersOf(HttpHeaders.ContentType, "application/json"))
        })

    private val onlineNetwork = object : Network {
        override fun isOnline(): Boolean {
            return true
        }
    }
    private val offlineNetwork = object : Network {
        override fun isOnline(): Boolean {
            return false
        }
    }


    @Test
    fun authAndSaveToken() = runTest{

        val response = """
            {
              "token": "saved_token",
              "record": {
                "id": "user-1",
                "email": "test@mail.ru",
                "name": "Test",
                "phone": "89999999999",
                "avatar": "",
                "verified": true,
                "created": "2026-11-10",
                "updated": "2026-11-10"
              }
            }
        """.trimIndent()


        val client = clientCreate(response)
        val tokenStorage = FakeTokenImpl()

        val repository = AuthRepositoryImpl(
            PBApi(client),
            tokenStorage,
            onlineNetwork
        )

        val result  = repository.auth(
            AuthRequest(
                "test@mail.ru",
                "password"
            )
        )

        assertIs<NetworkResult.Success<*>>(result)
        assertEquals("saved_token", tokenStorage.getToken())

        client.close()


    }


    @Test
    fun noInternetReturnError() = runTest {

        val client = clientCreate("{}")
        val repository = AppointmentRepositoryImpl(
            PBApi(client),
            offlineNetwork
        )


        val result = repository.getSlots("doctor-test", "2026-11-11")

        assertIs<NetworkResult.NoInternet>(result)

        client.close()
    }

    @Test
    fun authWrongPasswordReturnError() = runTest {

        val  response = """
            {
              "status": 400,
              "message": "Failed to authenticate.",
              "data": {}
            }
        """.trimIndent()

        val client = clientCreate(response,
            HttpStatusCode.BadRequest)
        val tokenStorage = FakeTokenImpl()

        val repository = AuthRepositoryImpl(
            PBApi(client),
            tokenStorage,
            onlineNetwork
        )

        val result  = repository.auth(
            AuthRequest(
                "test@mail.ru",
                "password"
            )
        )

        val error = assertIs<NetworkResult.Error>(result).errorResponse
        assertEquals(400,  error.status)
        assertEquals("Failed to authenticate.",  error.message)
        client.close()
    }

    @Test
    fun getSlotsReturns16Slots() = runTest {
        val slots = (0 until 16).joinToString(",") { index ->
            """
            {
                "time": "${9 + index / 2}:${if (index % 2 == 0) "00" else "30"}",
                "start": "2026-11-11T${9 + index / 2}:${if (index % 2 == 0) "00" else "30"}:00Z",
                "free": ${index % 2 == 0}
            }
            """.trimIndent()
        }

        val response = """
            {
                "doctor": "doctor-1",
                "date": "2026-11-11",
                "weekday": 3,
                "working": true,
                "slots": [$slots]
            }
        """.trimIndent()

        val client = clientCreate(response)
        val repository = AppointmentRepositoryImpl(
            PBApi(client),
            onlineNetwork
        )

        val result = repository.getSlots("doctor-1", "2026-11-11")

        val data = assertIs<NetworkResult.Success<*>>(result).data
        val slotsResult = data as Slots

        assertEquals(16, slotsResult.slots.size)
        for (i in 0 until 16) {
            assertEquals(i % 2 == 0, slotsResult.slots[i].free)
        }

        client.close()
    }

    @Test
    fun occupiedSlotReturnsStartFieldError() = runTest {
        val response = """
        {
            "status": 400,
            "message": "Это время уже занято.",
            "data": {
                "start": {
                    "code": "slot_taken",
                    "message": "Это время уже занято."
                }
            }
        }
    """.trimIndent()

        val client = clientCreate(response, HttpStatusCode.BadRequest)
        val repository = AppointmentRepositoryImpl(
            PBApi(client),
            onlineNetwork
        )

        val result = repository.postAppointments(
            UpdateAppointment(
                pet = "pet-test",
                doctor = "doctor-test",
                service = "service-test",
                start = "2026-11-11T10:00:00Z",
                comment = ""
            )
        )

        val error = assertIs<NetworkResult.Error>(result).errorResponse

        assertEquals(400, error.status)
        assertTrue(error.data!!.containsKey("start"))
        assertEquals("slot_taken", error.data["start"]?.code)
        assertEquals(
            "Это время уже занято.",
            error.data["start"]?.message
        )

        client.close()
    }


    @Test
    fun sessionTokenIsReadForEveryRequest() = runTest {
        val storage = FakeTokenImpl()
        val tokens = mutableListOf<String?>()
        val urls = mutableListOf<String>()
        val user = """{"id":"u","email":"e","name":"","phone":"","avatar":"","verified":false,"created":"","updated":""}"""
        val client = PBApiService("http://training.local:8090/api", storage::getToken)
            .create(MockEngine { request ->
                urls += request.url.toString()
                tokens += request.headers[HttpHeaders.Authorization]
                respond("""{"token":"token${tokens.size}","record":$user}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"))
            })
        client.use {
            val auth = AuthRepositoryImpl(PBApi(it), storage, onlineNetwork)
            assertIs<NetworkResult.Success<*>>(auth.auth(AuthRequest("e", "p")))
            repeat(2) { assertIs<NetworkResult.Success<*>>(auth.tokenRefresh()) }
        }
        assertEquals(listOf(null, "token1", "token2"), tokens)
        assertEquals("token3", storage.getToken())
        assertEquals(listOf("auth-with-password", "auth-refresh", "auth-refresh")
            .map { "http://training.local:8090/api/collections/users/$it" }, urls)
    }

    @Test
    fun socketTimeoutHasItsOwnResult() = runTest {
        PBApiService("http://localhost/api/", { null }).create(MockEngine {
            throw java.net.SocketTimeoutException("read timed out")
        }).use {
            val repository = AppointmentRepositoryImpl(PBApi(it), onlineNetwork)
            assertIs<NetworkResult.Timeout>(repository.getSlots("doctor", "2026-11-11"))
        }
    }
}
