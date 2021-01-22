package com.encora.musicplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.encora.musicplayer.R
import com.encora.musicplayer.utils.hide
import com.encora.musicplayer.utils.show
import kotlin.math.max

/**
 * A custom implementation of [RecyclerView] to support
 * Empty View & Loading animation.
 */
class CompleteRecyclerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    /**
     * Empty layout
     */
    private var emptyView: View? = null

    /**
     * Progress view
     */
    private var progressView: View? = null

    /**
     * Column width for grid layout
     */
    private var columnWidth: Int = 0

    init {
        hide()
        if (attrs != null) {
            val attrsArray = intArrayOf(android.R.attr.columnWidth)
            val array = context.obtainStyledAttributes(
                    attrs, attrsArray)
            columnWidth = array.getDimensionPixelSize(0, -1)
            array.recycle()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        show()
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(mAdapterObserver)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(mAdapterObserver)
        refreshState()
    }

    private fun refreshState() {
        adapter?.let {
            val noItems = 0 == it.itemCount
            if (noItems) {
                progressView?.hide()
                emptyView?.show()
                hide()
            } else {
                progressView?.hide()
                emptyView?.hide()
                show()
            }
        }
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
        this.emptyView?.hide()
    }

    fun setProgressView(progressView: View) {
        this.progressView = progressView
        this.progressView?.show()
    }

    fun setEmptyMessage(@StringRes mEmptyMessageResId: Int) {
        val emptyText = emptyView?.findViewById<TextView>(R.id.empty_title)
        emptyText?.setText(mEmptyMessageResId)
    }

    fun setEmptyIcon(@DrawableRes mEmptyIconResId: Int) {
        val emptyImage = emptyView?.findViewById<ImageView>(R.id.empty_image)
        emptyImage?.setImageResource(mEmptyIconResId)
    }

    fun showLoading() {
        emptyView?.hide()
        progressView?.show()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (layoutManager is GridLayoutManager) {
            val manager = layoutManager as GridLayoutManager
            if (columnWidth > 0) {
                val spanCount = max(1, measuredWidth / columnWidth)
                manager.spanCount = spanCount
            }
        }
    }

    /**
     * Observes for changes in the adapter and is triggered on change
     */
    private val mAdapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() = refreshState()
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = refreshState()
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = refreshState()
    }

}
