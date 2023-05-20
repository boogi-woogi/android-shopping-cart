import model.Page
import model.ShowingCartProductsPageRule
import org.junit.Assert.assertEquals
import org.junit.Test

class ShowingCartProductsPageRuleTest {

    private val products = listOf(
        ShoppingCartProduct(name = "아메리카노"),
        ShoppingCartProduct(name = "카페라떼"),
        ShoppingCartProduct(name = "밀크티"),
        ShoppingCartProduct(name = "얼그레이"),
        ShoppingCartProduct(name = "녹차"),
        ShoppingCartProduct(name = "카라멜 마끼아또"),
        ShoppingCartProduct(name = "스콘")
    )

    @Test
    fun `페이지에 띄워줄 항목을 페이지마다 앞에서부터 세개씩 끊어서 제공한다`() {
        // given
        val page = Page(1)

        // when
        val actual = ShowingCartProductsPageRule.getProductsOfPage(
            products = products,
            page = page
        )

        // then
        val expected = listOf(
            ShoppingCartProduct(name = "얼그레이"),
            ShoppingCartProduct(name = "녹차"),
            ShoppingCartProduct(name = "카라멜 마끼아또"),
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `장바구니의 전체 상품 개수에서 하나를 빼고 3으로 나누어 마지막 페이지를 계산한다`() {
        // given: 전체 상품 개수가 16개인 경우
        val totalProductsSize = 16

        // when
        val actual = ShowingCartProductsPageRule.getPageOfEnd(totalProductsSize)

        // then: 마지막 페이지는 5페이지다
        val expected = Page(5)

        assertEquals(expected, actual)
    }

    @Test
    fun `장바구니의 전체 상품 개수가 0이라면 0페이지가 마지막 페이지다`() {
        // given: 전체 상품 개수가 0개인 경우
        val totalProductsSize = 0

        // when
        val actual = ShowingCartProductsPageRule.getPageOfEnd(totalProductsSize)

        // then: 마지막 페이지는 5페이지다
        val expected = Page(0)

        assertEquals(expected, actual)
    }
}