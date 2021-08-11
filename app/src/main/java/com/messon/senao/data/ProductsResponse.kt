package com.messon.senao.data

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("data") val data: List<ProductBean>
)