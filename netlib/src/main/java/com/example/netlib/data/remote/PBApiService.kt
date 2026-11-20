package com.example.netlib.data.remote

import android.net.ConnectivityManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * PBApiService - настройка http client
 *
 * @param defaultUrl - путь до сервера по умолчанию
 * @param getToken - токен авторизации
 *
 */
internal class PBApiService(
    private val defaultUrl : String,
    private val getToken: () -> String?
) {


    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }


    fun create(): HttpClient =
        HttpClient(OkHttp) {

            expectSuccess = true

            install(Logging){

                level = LogLevel.HEADERS

                sanitizeHeader { header ->
                    header == HttpHeaders.Authorization
                }

            }

            install(ContentNegotiation){
                json(json)
            }

            install(HttpTimeout){
                requestTimeoutMillis = 30000
            }

            defaultRequest {
                defaultUrl
                getToken?.let {
                     header(HttpHeaders.Authorization, it)
                }
            }

        }
}