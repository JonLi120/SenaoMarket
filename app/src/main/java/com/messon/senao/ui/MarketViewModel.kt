package com.messon.senao.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.messon.senao.data.ProductBean
import com.messon.senao.db.entities.Product
import com.messon.senao.remote.MarketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(private val repo: MarketRepository):
    ViewModel() {
    private val mDisposable = CompositeDisposable()

    private val originProductList = mutableListOf<ProductBean>()
    private val _productList = MutableLiveData<List<ProductBean>>()
    val productList: LiveData<List<ProductBean>> get() = _productList

    fun getProductList() {
        repo.getMartList()
            .subscribeOn(Schedulers.io())
            .map {
                repo.insertAndDeleteProducts(it.data.map { bean ->
                    Product(
                        martName = bean.martName,
                        martShortName = bean.martShortName,
                        finalPrice = bean.finalPrice,
                        imageUrl = bean.imageUrl,
                        martId = bean.martId
                    )
                })
                originProductList.addAll(it.data)
                it.data
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                _productList.value = it
            }, onError = {
                Timber.e(it)
                getLocalProductList()
            }).addTo(mDisposable)

    }

    private fun getLocalProductList() {
        repo.getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                _productList.value = it
            }, onError = {
                Timber.e(it)
            })
            .addTo(mDisposable)
    }

    fun filter(query: String?) {
        _productList.value = if (query.isNullOrEmpty()) {
            originProductList
        } else {
            originProductList.filter { bean -> bean.martName.contains(query) }
        }
    }

    override fun onCleared() {
        mDisposable.clear()
        super.onCleared()
    }
}