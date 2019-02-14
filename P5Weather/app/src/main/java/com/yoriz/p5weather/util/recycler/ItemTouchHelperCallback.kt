package com.yoriz.yorizframework.util.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper

/**
 * Created by yoriz
 * on 2018/12/18 3:11 PM.
 */
class ItemTouchHelperCallback(private val moveAndSwipedOnListener: OnMoveAndSwipedListener, private val isAction: Boolean) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Int {
        if (isAction) {
            return if (recyclerView.layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
                //单列的RecyclerView支持上下拖动和左右侧滑
                //改为不支持上下
                makeMovementFlags(
                        ItemTouchHelper.UP or
                                ItemTouchHelper.DOWN or
                                ItemTouchHelper.LEFT or
                                ItemTouchHelper.RIGHT,
                        ItemTouchHelper.START or
                                ItemTouchHelper.END)
            } else {
                //多列的RecyclerView支持上下左右拖动和不支持左右侧滑
                makeMovementFlags(
                        ItemTouchHelper.UP or
                                ItemTouchHelper.DOWN or
                                ItemTouchHelper.LEFT or
                                ItemTouchHelper.RIGHT,
                        0)
            }
        }
        return makeMovementFlags(0, 0)
    }

    override fun onMove(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, target: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
        if (!isAction) return false
        //如果两个item不是同一个类型的，不让他拖拽
        if (viewHolder.itemViewType != target.itemViewType) return false
        moveAndSwipedOnListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
        moveAndSwipedOnListener.onItemDismiss(viewHolder.adapterPosition)
    }
}