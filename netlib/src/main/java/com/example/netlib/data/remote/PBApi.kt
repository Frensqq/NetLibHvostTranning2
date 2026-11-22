package com.example.netlib.data.remote

import com.example.netlib.data.remote.DTO.AppointmentDTO
import com.example.netlib.data.remote.DTO.AppointmentListDTO
import com.example.netlib.data.remote.DTO.AppointmentUpdateDTO
import com.example.netlib.data.remote.DTO.AuthRequestDTO
import com.example.netlib.data.remote.DTO.AuthResponseDTO
import com.example.netlib.data.remote.DTO.CancelDTO
import com.example.netlib.data.remote.DTO.ClinicListDTO
import com.example.netlib.data.remote.DTO.DoctorListDTO
import com.example.netlib.data.remote.DTO.FileUploadDTO
import com.example.netlib.data.remote.DTO.PatchUserDTO
import com.example.netlib.data.remote.DTO.PetDTO
import com.example.netlib.data.remote.DTO.PetListDTO
import com.example.netlib.data.remote.DTO.PetUpdateDTO
import com.example.netlib.data.remote.DTO.RegistrationDTO
import com.example.netlib.data.remote.DTO.ServiceListDTO
import com.example.netlib.data.remote.DTO.SlotsDTO
import com.example.netlib.data.remote.DTO.UserDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

/**
 * PBApi - класс с запросами к серверу
 *
 * @param client - httpClient
 */
internal class PBApi(
        private val client: HttpClient
) {


    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }

    private suspend inline fun< reified  D> get(
        path: String,
        page: Int? = null,
        perPage: Int? = null,
        sort: String? = null,
        filter: String? = null,
        expand: String? = null
    ): D = client.get(path){
        page?.let { parameter("page", it) }
        perPage?.let { parameter("perPage", it) }
        sort?.let { parameter("sort", it) }
        filter?.let { parameter("filter", it) }
        expand?.let { parameter("expand", it) }
    }.body()


    private suspend inline fun <reified D, reified Body> post(
        path: String,
        data: Body
    ) : D = client.post(path) {
        setBody(data)
        contentType(ContentType.Application.Json)
    }.body()

    private suspend fun delete(path: String){
        client.delete(path)
    }

    private fun files(
        vararg values: Pair<String, FileUploadDTO>
    ) = values
        .filter { it.second != null }
        .associate { it.first to listOf(it.second!!) }

    private inline fun<reified D> fields( data: D): Map<String , String> =
        json.encodeToJsonElement(data)
            .jsonObject
            .mapNotNull { (key, value) ->
                if (value == JsonNull) null
                else key to value.let {
                    if (it is JsonPrimitive) it.content else it.toString()
                }
            }.toMap()

    private inline fun <reified D> multipartBody(
        data: D,
        files: Map<String, List<FileUploadDTO>>
    ) = MultiPartFormDataContent(
        formData {
            fields(data).forEach { (key, value) ->
                append(key, value)
            }

            files.forEach { (field, list) ->
                list.forEach { file ->
                    append(
                        field,
                        file.bytes,
                        Headers.build {
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=\"${file.name}\""
                            )
                            append(HttpHeaders.ContentType, file.mimeType)
                        }
                    )
                }
            }
        }
    )

    private suspend inline fun <reified T, reified D> postMultipart(
        path: String,
        data: D,
        files: Map<String, List<FileUploadDTO>>
    ): T = client.post(path) {
        setBody(multipartBody(data, files))
    }.body()

    private suspend inline fun <reified T, reified D> patchMultipart(
        path: String,
        data: D,
        files: Map<String, List<FileUploadDTO>>
    ): T = client.patch(path) {
        setBody(multipartBody(data, files))
    }.body()


    //пользователь
    suspend fun register(
        data: RegistrationDTO
    ): UserDTO = post(
        "collections/users/records",
        data
    )
    suspend fun auth(
        data: AuthRequestDTO
    ): AuthResponseDTO = post(
        "collections/users/auth-with-password",
        data
    )

    suspend fun tokenRefresh() : AuthResponseDTO =
        post("collections/users/auth-refresh",
            buildJsonObject {  })

    suspend fun getUser(
        userId: String
    ): UserDTO = get(
        "collections/users/records/${userId}"
    )
    suspend fun patchUser(
        userId: String,
        data: PatchUserDTO,
        avatar: FileUploadDTO
    ): UserDTO = patchMultipart(
        "collections/users/records/${userId}",
        data,
        files("avatar" to avatar)
    )



    //Catalogs

    suspend fun getService(
        page: Int? = null,
        perPage: Int? = null,
        sort: String? = null,
    ): ServiceListDTO = get(
        "collections/services/records",
        page = page, perPage = perPage, sort = sort
    )

    suspend fun getClinics(
        page: Int? = null,
        perPage: Int? = null,
        sort: String? = null,
    ): ClinicListDTO = get(
        "collections/clinics/records",
        page = page, perPage = perPage, sort = sort
    )

    suspend fun getDoctors(
        page: Int? = null,
        perPage: Int? = null,
        sort: String? = "-clinic",
        filter: String? = null,
    ): DoctorListDTO = get(
        "collections/doctors/records",
        page = page, perPage = perPage, sort = sort,
        expand = "clinic", filter = filter
    )


    //pet
    suspend fun getPets(
        page: Int? = null,
        perPage: Int? = null,
        sort: String? = null,
    ): PetListDTO = get(
        "collections/pets/records",
        page = page, perPage = perPage, sort = sort
    )

    suspend fun postPet(
        data: PetUpdateDTO,
        photo: FileUploadDTO
    ): PetDTO = postMultipart(
        "collections/pets/records",
        data,
        files("photo" to photo)
    )
    suspend fun patchPet(
        id: String,
        data: PetUpdateDTO,
        photo: FileUploadDTO
    ): PetDTO = patchMultipart(
        "collections/pets/records/$id",
        data,
        files("photo" to photo)
    )

    suspend fun deletePet(
        id: String
    ): Unit = delete("collections/pets/records/${id}")


    //Appointment
    suspend fun getSlots(doctorId: String, date: String): SlotsDTO =
        client.get("hvost/slots") {
            parameter("doctor", doctorId)
            parameter("date", date)
        }.body()

    suspend fun getAppointments(
        filter: String?,
        sort: String?,
        page: Int? = 1,
        perPage: Int? = 20
    ): AppointmentListDTO = get(
        "collections/appointments/records",
        expand = "pet,doctor,service,clinic",
        filter = filter,
        sort = sort,
        page = page,
        perPage = perPage
    )

    suspend fun postAppointments(
        data: AppointmentUpdateDTO
    ): AppointmentDTO =
        post(
            "collections/appointments/records",
            data
        )



    suspend fun canselAppointment(
        id: String,
        data: CancelDTO,
    ): AppointmentDTO =
        post("hvost/appointments/${id}/cancel",
            data)

}