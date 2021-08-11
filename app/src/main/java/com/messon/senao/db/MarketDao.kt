package com.messon.senao.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.messon.senao.data.ProductBean
import com.messon.senao.db.entities.Product
import io.reactivex.rxjava3.core.Single

@Dao
interface MarketDao {

    @Query("DELETE FROM product_table")
    fun deleteAll()

    @Insert
    fun insertProducts(products: List<Product>)

    @Transaction
    fun insertAndDeleteProducts(products: List<Product>) {
        deleteAll()
        insertProducts(products)
    }

    @Query("SELECT * FROM product_table")
    fun getProducts(): Single<List<ProductBean>>
}