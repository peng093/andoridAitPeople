package com.example.atpeople.myapplication.util;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

public class RvManagerUtils {
    public static RvManagerUtils getInstance(){
        return RvManagerUtils.LayoutManagerHolder.rvManagerUtils;
    }

    private static class LayoutManagerHolder{
        private static RvManagerUtils rvManagerUtils = new RvManagerUtils();
    }

    /**
     * recycleview垂直方向
     *
     * @param context the context
     * @return the linear layout manager
     */
    public LinearLayoutManager getRecycleviiewVertical(Context context){
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        return manager;
    }

    /**
     * recycleview水平方向
     *
     * @param context the context
     * @return the linear layout manager
     */
    public LinearLayoutManager getRecycleviiewHorizontal(Context context){
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        return manager;
    }

    /**
     * recycleview表格
     *
     * @param context the context
     * @param size    the size
     * @return the grid layout manager
     */
    public GridLayoutManager getRecycleviiewGrid(Context context, int size){
        GridLayoutManager manager = new GridLayoutManager(context, size);
        return manager;
    }
}
