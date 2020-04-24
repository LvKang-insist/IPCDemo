package com.www.ipcdemo.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class ItemTouchCallback: ItemTouchHelper.Callback() {

    interface OnItemTouchListener {
        fun onMove(fromPosition: Int, toPosition: Int)
        fun onLongClick(holder: RecyclerView.ViewHolder?)
        fun onUp(viewHolder: RecyclerView.ViewHolder)
    }

    var onItemTouchListener: OnItemTouchListener? = null

    /**
     * 返回我们要监控的方向，上下左右，我们做的是上下拖动，要返回都是UP和DOWN
     * 关键坑爹的是下面方法返回值只有1个，也就是说只能监控一个方向。
     * 不过点入到源码里面有惊喜。源码标记方向如下：
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        var dragFlags = 0
        if (recyclerView.layoutManager is GridLayoutManager) {
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        } else {
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }
        // 支持左右滑动(删除)操作, swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END
        val swipeFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * 拖动某个item的回调，在return前要更新item位置，调用notifyItemMoved（draggedPosition，targetPosition）
     * viewHolder:正在拖动item
     * target：要拖到的目标
     * @return true 表示消费事件
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        onItemTouchListener?.onMove(fromPosition, toPosition)
        return true;
    }

    /**
     * 左右拖动的回调
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    /**
     * 官方文档如下：返回true 当前item可以被拖动到目标位置后，直接”落“在target上，其他的上面的item跟着“落”，
     * 所以要重写这个方法，不然只是拖动的item在动，target item不动，静止的
     * Return true if the current ViewHolder can be dropped over the the target ViewHolder.
     */
    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    /**
     * 是否开启长安拖动
     */
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    /**
     * 长按选中Item的时候开始调用
     * 长按高亮
     * @param viewHolder
     * @param actionState
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            onItemTouchListener?.onLongClick(viewHolder)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        onItemTouchListener?.onUp(viewHolder)
    }

}