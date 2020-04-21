package fr.isen.emelian.mypharma.remote

import android.webkit.WebStorage
import fr.isen.emelian.mypharma.model.PlaceDetail
import fr.isen.emelian.mypharma.model.RootObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

public interface IGoogleApiService {
    @GET
    fun getNearbyPlaces(@Url url:String): Call<RootObject>

    @GET
    fun getDetailPlaces(@Url url:String): Call<PlaceDetail>

}