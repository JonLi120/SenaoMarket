package com.messon.senao.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.messon.senao.data.ProductBean
import com.messon.senao.databinding.ActivityMarketListBinding
import com.messon.senao.databinding.ItemProductBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MarketListActivity : AppCompatActivity() {

    lateinit var binding: ActivityMarketListBinding
    private val mAdapter by lazy { ProductAdapter() }
    private val viewModel by viewModels<MarketViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMarketListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setupListener()
        observeViewModel()
        viewModel.getProductList()
    }

    private fun initView() {
        binding.rcv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MarketListActivity)
            adapter = mAdapter
        }
    }

    private fun setupListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filter(newText)
                return false
            }

        })

        mAdapter.onItemClick = { view, bean ->
            view.transitionName = bean.martId

            val intent = Intent(this, ProductInfoActivity::class.java).also {
                it.putExtra("product", bean)
            }

            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                view,
                bean.martId
            )
            startActivity(intent, options.toBundle())
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            productList.observe(this@MarketListActivity, {
                mAdapter.update(it)
            })
        }
    }

    class ProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val df = DecimalFormat("$ #,###")
        private val list = mutableListOf<ProductBean>()
        var onItemClick: ((View, ProductBean) -> Unit)? = null

        fun update(newList: List<ProductBean>) {
            list.clear()
            list.addAll(newList)
            notifyDataSetChanged()
        }

        override fun getItemCount() = list.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ProductVH(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ProductVH) {
                holder.onBind(list[position])
            }
        }

        inner class ProductVH(private val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
            init {
                itemView.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribeBy {

                    onItemClick?.invoke(binding.imgPhoto, list[adapterPosition])
                }
            }
            fun onBind(bean: ProductBean) = binding.apply {
                Glide.with(root)
                    .load(bean.imageUrl)
                    .into(imgPhoto)

                tvName.text = bean.martName
                tvPrice.text = df.format(bean.finalPrice)
            }
        }
    }
}