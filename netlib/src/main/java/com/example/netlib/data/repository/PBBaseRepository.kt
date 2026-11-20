package com.example.netlib.data.repository

import android.R
import com.example.netlib.data.mapper.toDomain
import com.example.netlib.data.remote.DTO.ApiErrorDTO
import com.example.netlib.domain.models.ApiError
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.network.Network
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.ContentConvertException
import io.ktor.serialization.JsonConvertException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import okio.IOException
import java.util.concurrent.TimeoutException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.Throws

abstract class PBBaseRepository(
    protected val network: Network
) {

   protected suspend fun <T> saveApiCall(
        apiCall: suspend () -> T
    ): NetworkResult<T> {

       if (!network.isOnline()){
           return NetworkResult.NoInternet
       }

       return try {
           NetworkResult.Success(apiCall())
       } catch (e: TimeoutException){
           return NetworkResult.Timeout
       } catch (e: IOException){
           return NetworkResult.NoInternet
       } catch (e: ResponseException){

           val status = e.response.status.value

           val text = try {
               e.response.bodyAsText()
           } catch (e: Exception) {
               return NetworkResult.Error(
                   ApiError(
                       status = status,
                       message = "Не удалось прочитать тело ответа ${e.message}"
                   )
               )
           }

           val error = try {
               Json { ignoreUnknownKeys = true }
                   .decodeFromString<ApiErrorDTO>(text)
                   .toDomain()

           }  catch (e: SerializationException) {
               return NetworkResult.ErrorSerialization(
                   message = "Ошибка сериализации ответа сервера ${e.message}",
                   status = status
               )

           } catch (e: Exception) {
               return NetworkResult.ErrorSerialization(
                   message = "Некорректный формат ошибки: ${e.message}",
                   status = status
               )
           }

           NetworkResult.Error(error)
       }catch (e: ContentConvertException){
           NetworkResult.Error(
               ApiError(
                   message = e.cause?.message?: e.message ?: "Ошибка конвертации ответа сервера"
               )
           )
       } catch (e: JsonConvertException){
           NetworkResult.ErrorSerialization(
                   message = e.cause?.message?: e.message ?: "Ошибка конвертации json ответа сервера"
           )
       } catch (e: SerializationException) {
            NetworkResult.ErrorSerialization(
               message = e.message?: "Ошибка сериализации ответа сервера",
           )

       } catch (e: Exception) {
            NetworkResult.Error(
                ApiError(
                -1,
               message = "Unknown error: ${e.message}"
                )
           )
       }




    }



}