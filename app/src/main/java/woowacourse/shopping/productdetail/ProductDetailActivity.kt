package woowacourse.shopping.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import woowacourse.shopping.R
import woowacourse.shopping.common.CountPickerListener
import woowacourse.shopping.database.ShoppingDBAdapter
import woowacourse.shopping.database.product.ShoppingDao
import woowacourse.shopping.databinding.ActivityProductDetailBinding
import woowacourse.shopping.getSerializableCompat
import woowacourse.shopping.model.ProductUiModel
import woowacourse.shopping.shoppingcart.ShoppingCartActivity
import woowacourse.shopping.util.handleMissingSerializableData

class ProductDetailActivity : AppCompatActivity(), ProductDetailContract.View {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var presenter: ProductDetailPresenter
    private lateinit var dialog: ProductCountPickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)

        setUpPresenter()
        setUpProductDetailToolbar()
    }

    private fun setUpPresenter() {
        intent.getSerializableCompat<ProductUiModel>(PRODUCT_KEY)?.let {
            presenter = ProductDetailPresenter(
                view = this,
                product = it,
                latestViewedProduct = intent.getSerializableCompat(LATEST_VIEWED_PRODUCT_KEY),
                repository = ShoppingDBAdapter(
                    shoppingDao = ShoppingDao(this)
                )
            )
        } ?: return handleMissingSerializableData()
    }

    private fun setUpProductDetailToolbar() {
        setSupportActionBar(binding.toolbarProductDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_close, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_close -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setUpProductDetailView(
        product: ProductUiModel,
        navigateToLatestViewedProductView: () -> Unit,
    ) {
        Glide.with(this)
            .load(product.imageUrl)
            .into(binding.imageProductDetail)

        setUpProductCountPickerDialog(product)
        with(binding) {
            binding.product = product
            layoutLatestViewedProduct.setOnClickListener {
                navigateToLatestViewedProductView()
            }
            buttonPutToShoppingCart.setOnClickListener {
                dialog.show(
                    supportFragmentManager, ProductCountPickerDialog.TAG
                )
            }
        }
    }

    override fun setUpLatestViewedProductView(product: ProductUiModel?) {
        product?.let {
            binding.latestViewedProduct = product
        } ?: run {
            binding.layoutLatestViewedProduct.isVisible = false
        }
    }

    private fun setUpProductCountPickerDialog(product: ProductUiModel) {
        val countPickerListenerImpl = object : CountPickerListener {

            override fun onPlus() {
                presenter.plusCartProductCount()
            }

            override fun onMinus() {
                presenter.minusCartProductCount()
            }
        }
        val addingCartListenerImpl = object : AddingCartListener {

            override fun onAdded() {
                presenter.addToCart()
            }
        }

        dialog = ProductCountPickerDialog.newInstance(
            product = product,
            countPickerListener = countPickerListenerImpl,
            addingCartListener = addingCartListenerImpl
        )
    }

    override fun setUpDialogProductCountView(count: Int) {
        dialog.setTextProductCount(count)
    }

    override fun setUpDialogTotalPriceView(totalPrice: Int) {
        dialog.setTextTotalPrice(totalPrice)
    }

    override fun navigateToCartView() {
        startActivity(
            ShoppingCartActivity.getIntent(this)
        )
        finish()
    }

    override fun navigateToProductDetailView(product: ProductUiModel) {
        val intent = getIntent(
            context = this,
            product = product,
            latestViewedProduct = null
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        startActivity(intent)
    }

    companion object {
        private const val PRODUCT_KEY = "product"
        private const val LATEST_VIEWED_PRODUCT_KEY = "latest_viewed_product"

        fun getIntent(
            context: Context,
            product: ProductUiModel,
            latestViewedProduct: ProductUiModel?,
        ): Intent {

            return Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_KEY, product)
                putExtra(LATEST_VIEWED_PRODUCT_KEY, latestViewedProduct)
            }
        }
    }
}
