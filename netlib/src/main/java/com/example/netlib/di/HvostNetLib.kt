package com.example.netlib.di

import android.content.Context
import com.example.netlib.data.network.NetworkImpl
import com.example.netlib.data.remote.PBApi
import com.example.netlib.data.remote.PBApiService
import com.example.netlib.data.repository.AppointmentRepositoryImpl
import com.example.netlib.data.repository.AuthRepositoryImpl
import com.example.netlib.data.repository.CatalogRepositoryImpl
import com.example.netlib.data.repository.UserRepositoryImpl
import com.example.netlib.data.storage.TokenStorageImpl
import com.example.netlib.domain.repository.AppointmentRepository
import com.example.netlib.domain.repository.AuthRepository
import com.example.netlib.domain.repository.CatalogRepository
import com.example.netlib.domain.repository.PetRepository
import com.example.netlib.domain.repository.UserRepository
import com.example.netlib.domain.storage.TokenStorage
class HvostNetLib(
    context: Context,
    baseUrl: String,
    private val tokenStorage: TokenStorage = TokenStorageImpl(context)
){

    private val clientFactory = PBApiService(
        defaultUrl = baseUrl,
        getToken = {tokenStorage.getToken()}
    )

    private val httpClient = clientFactory.create()

    private val api = PBApi(httpClient)

    private val networkMonitor = NetworkImpl(context.applicationContext)


    val authRepository: AuthRepository
    = AuthRepositoryImpl(
        api,
        tokenStorage,
        networkMonitor
    )

    val userRepository: UserRepository =
        UserRepositoryImpl(api, networkMonitor)

    val catalogRepository: CatalogRepository =
        CatalogRepositoryImpl(api, networkMonitor)

    val petRepository: PetRepository =
        PetRepositoryImpl(api, networkMonitor)

    val appointmentRepository: AppointmentRepository =
        AppointmentRepositoryImpl(api,networkMonitor)
}