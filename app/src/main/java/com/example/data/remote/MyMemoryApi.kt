package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class MyMemoryResponse(
    @Json(name = "responseData") val responseData: MyMemoryResponseData?,
    @Json(name = "responseStatus") val responseStatus: Int? = null
)

@JsonClass(generateAdapter = true)
data class MyMemoryResponseData(
    @Json(name = "translatedText") val translatedText: String?
)

interface MyMemoryApi {
    @GET("get")
    suspend fun translate(
        @Query("q") text: String,
        @Query("langpair") langPair: String
    ): MyMemoryResponse

    companion object {
        private const val BASE_URL = "https://api.mymemory.translated.net/"

        fun create(): MyMemoryApi {
            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(MyMemoryApi::class.java)
        }
    }
}
