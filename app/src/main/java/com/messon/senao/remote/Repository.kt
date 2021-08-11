package com.messon.senao.remote

import com.messon.senao.db.MarketDao
import com.messon.senao.db.entities.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRepository @Inject constructor(
    private val remoteDataSource: ApiService,
    private val localDataSource: MarketDao
){
    fun getMartList() = remoteDataSource.getMartList()

    fun insertAndDeleteProducts(products: List<Product>) = localDataSource.insertAndDeleteProducts(products)

    fun getProducts() = localDataSource.getProducts()
}