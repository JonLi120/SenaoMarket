package com.messon.senao.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductBean(val martShortName: String,
                       val martName: String,
                       val finalPrice: Int,
                       val imageUrl: String,
                       val martId: String) : Parcelable