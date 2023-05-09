package woowacourse.shopping

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.database.ShoppingRepository
import woowacourse.shopping.productdetail.ProductUiModel
import woowacourse.shopping.shopping.ShoppingContract
import woowacourse.shopping.shopping.ShoppingPresenter

class ShoppingPresenterTest {

    lateinit var presenter: ShoppingContract.Presenter
    lateinit var view: ShoppingContract.View
    lateinit var repository: ShoppingRepository

    @Before
    fun setUp() {
        view = mockk()
        repository = mockk()
        presenter = ShoppingPresenter(view, repository)
    }

    @Test
    fun `저장소로부터 상품 목록을 받아와서 뷰를 초기화한다`() {
        // given
        val slot = slot<List<ProductUiModel>>()
        every { view.setUpShoppingView(capture(slot)) } just Runs
        every { repository.loadProducts() } returns listOf(
            ProductUiModel(name = "아메리카노"),
            ProductUiModel(name = "카페라떼")
        )

        // when
        presenter.loadProducts()

        // then
        val actual = slot.captured
        val expected = listOf(
            ProductUiModel(name = "아메리카노"),
            ProductUiModel(name = "카페라떼")
        )
        assertEquals(actual, expected)
        verify { view.setUpShoppingView(actual) }
    }
}
