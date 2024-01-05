package com.norm.myretrofitlesson.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface ProductAPI {
    @GET("products/{id}")
    suspend fun getProducts(@Path("id") id: Int): Product
}