package com.messon.senao.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.bumptech.glide.Glide
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.messon.senao.data.ProductBean
import com.messon.senao.databinding.ActivityProductInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class ProductInfoActivity: AppCompatActivity() {

    lateinit var binding: ActivityProductInfoBinding
    private val product: ProductBean? by lazy {
        intent.extras?.getParcelable("product")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInfoBinding.inflate(layoutInflater)
        binding.imgPhoto.transitionName = product?.martId
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.sharedElementEnterTransition = buildContainerTransform()
        window.sharedElementReturnTransition = buildContainerTransform()
        setContentView(binding.root)

        initView()
        setupListener()
    }

    private fun buildContainerTransform() =
        MaterialContainerTransform().apply {
            addTarget(binding.imgPhoto)
            duration = 300
            interpolator = FastOutSlowInInterpolator()
            fadeMode = MaterialContainerTransform.FADE_MODE_IN
        }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val df = DecimalFormat("$#,###")
        product?.run {
            Glide.with(binding.root)
                .load(imageUrl)
                .into(binding.imgPhoto)

            binding.tvNumberId.text = "商品編號：${martId}"
            binding.tvProductName.text = martShortName
            binding.tvPrice.text = df.format(finalPrice)
        }
    }

    private fun setupListener() {
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}