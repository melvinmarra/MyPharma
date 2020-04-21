package fr.isen.emelian.mypharma.common

import fr.isen.emelian.mypharma.model.Results
import fr.isen.emelian.mypharma.remote.IGoogleApiService
import fr.isen.emelian.mypharma.remote.RetrofitClient

object Common {
    private val GOOGLE_API_URL = "https://maps.googleapis.com/"

    var currentResult: Results?=null

    val googleApiService : IGoogleApiService
        get() = RetrofitClient.getClient(GOOGLE_API_URL).create((IGoogleApiService::class.java))

}