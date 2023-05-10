package woowacourse.shopping.shopping

import model.RecentViewedProducts
import woowacourse.shopping.model.ProductUiModel

interface ShoppingContract {

    interface View {

        fun setUpShoppingView(products: List<ProductUiModel>, recentViewedProducts: List<ProductUiModel>)
    }

    interface Presenter {

        val recentViewedProducts: RecentViewedProducts

        fun loadProducts()

        fun addToRecentViewedProduct(id: Int)
    }
}
