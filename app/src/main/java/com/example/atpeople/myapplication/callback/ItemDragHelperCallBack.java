package com.example.atpeople.myapplication.callback;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;


/**
 * Created by wj on 2018/2/24.
 * 长按拖拽
 */

public class ItemDragHelperCallBack extends ItemTouchHelper.Callback {

    private OnItemDragListener onItemDragListener;

    public ItemDragHelperCallBack(OnItemDragListener onItemDragListener) {
        this.onItemDragListener = onItemDragListener;
    }

    /**
     * 返回可以滑动的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        int dragFlags ;
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
            //网格布局管理器允许上下左右拖动
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            //其他布局管理器允许上下拖动
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        return makeMovementFlags(dragFlags, 0);
    }

    /**
     * 拖拽到新位置时候的回调方法
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //不同Type之间不允许移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        if (onItemDragListener != null) {
            onItemDragListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //  actionState==2 开始拖动  0拖动完成
        Log.e(TAG, "onSelectedChanged: 拖动状态"+actionState );
        onItemDragListener.onItemMoveStartAndEnd(viewHolder,actionState);
    }

    /**
     * 当用户左右滑动的时候执行的方法
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        onItemDragListener.clearView(recyclerView, viewHolder);
    }

    public void setOnItemDragListeber(OnItemDragListener onItemDragListener) {
        this.onItemDragListener = onItemDragListener;
    }
    public interface OnItemDragListener {
        void onItemMove(int startPos,int endPos);
        void onItemMoveStartAndEnd(RecyclerView.ViewHolder viewHolder, int status);
        void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    }
}
