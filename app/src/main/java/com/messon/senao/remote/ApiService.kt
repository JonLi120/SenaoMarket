package com.messon.senao.remote

import com.messon.senao.data.ProductsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ApiService {

    @GET("marttest.jsp")
    fun getMartList() : Single<ProductsResponse>
}