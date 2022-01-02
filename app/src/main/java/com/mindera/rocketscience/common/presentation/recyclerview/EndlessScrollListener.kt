package com.mindera.rocketscience.common.presentation.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mindera.rocketscience.common.utils.BooleanCallback
import com.mindera.rocketscience.common.utils.UnitCallback

class EndlessScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val pageSize: Int,
    private val onLoadMore: UnitCallback,
    private val isLoading: BooleanCallback,
    private val hasMoreItems: BooleanCallback
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        handleItemsLoading(
            layoutManager.itemCount,
            layoutManager.childCount
        )
    }

    private fun handleItemsLoading(itemCount: Int, childCount: Int) {
        if (hasMoreItems() && !isLoading()) {
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            if (itemCount >= pageSize && firstVisibleItemPosition >= 0 && (childCount + firstVisibleItemPosition) >= itemCount) {
                onLoadMore()
            }
        }
    }
}