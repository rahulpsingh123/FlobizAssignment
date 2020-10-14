package com.l.fininassignment.network

import com.l.fininassignment.network.MyLoggingInterceptor.provideOkHttpLogging
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class APINewsManager private constructor() {
    private val baseUrl = "https://reqres.in/"

    private fun createRetrofitService(): Easy2LivNewsApiClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(provideOkHttpLogging())
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(Easy2LivNewsApiClient::class.java)
    }

    private val service: Easy2LivNewsApiClient
        get() {
            return createRetrofitService()
        }

    private interface Easy2LivNewsApiClient {
        @GET("api/users")
        fun getData(@QueryMap map: Map<String, Int>): Call<ResponseBody>
    }


    fun getPaginationData(requestBody: Map<String, Int>): Call<ResponseBody> {
        return service.getData(requestBody)
    }

    companion object {
        private var myInstance: APINewsManager? = null
        val instance: APINewsManager?
            get() {
                if (myInstance == null) {
                    myInstance = APINewsManager()
                }
                return myInstance
            }
    }

}