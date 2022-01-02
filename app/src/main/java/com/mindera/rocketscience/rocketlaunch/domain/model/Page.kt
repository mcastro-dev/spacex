package com.mindera.rocketscience.rocketlaunch.domain.model

data class Page(
    val number: Int,
    val expectedItemsPerPage: Int = DEFAULT_ITEMS_PER_PAGE
) {
    companion object {
        const val DEFAULT_ITEMS_PER_PAGE = 10
        private const val FIRST_PAGE_NUMBER = 0

        fun first() = Page(FIRST_PAGE_NUMBER)
        fun first(expectedItemsPerPage: Int) = Page(FIRST_PAGE_NUMBER, expectedItemsPerPage)
    }

    fun next() = Page(number + 1, expectedItemsPerPage)
}