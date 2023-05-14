package woowacourse.shopping.shopping

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.RecentViewedProductUiModel

class RecentViewedRecyclerAdapter(private val products: List<RecentViewedProductUiModel>) :
    RecyclerView.Adapter<RecentViewedProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewedProductViewHolder {

        return RecentViewedProductViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecentViewedProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}
