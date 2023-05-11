package model

@JvmInline
value class Price(val value: Int) {

    init {
        require(value > 0) {
            PRICE_AMOUNT_ERROR
        }
    }

    companion object {
        private const val PRICE_AMOUNT_ERROR = "가격은 0원보다 커야합니다."
    }
}