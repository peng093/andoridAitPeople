package com.example.atpeople.myapplication.ui;

import android.app.Service;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ViewGroup;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.callback.ItemDragHelperCallBack;
import com.example.atpeople.myapplication.ui.notify.NotifyAdapter;
import com.example.atpeople.myapplication.ui.notify.model.NotifyBean;
import com.example.atpeople.myapplication.util.NotifyUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Create by peng on 2020/10/26
 */
public class ListDrag extends BaseActivity {
    @BindView(R.id.rec_list)
    RecyclerView rec_list;


    @Override
    protected int initLayout() {
        return R.layout.activity_drag_list;
    }

    @Override
    protected void initView() {
        rec_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void initData() {
        NotifyAdapter adapter = new NotifyAdapter( new ArrayList<>(),this);
        rec_list.setAdapter(adapter);
        // 设置数据源
        List<NotifyBean> list=NotifyUtil.initDatas();
        list.addAll(NotifyUtil.initDatas());
        adapter.setNewData(list);

        ItemDragHelperCallBack callBack = new ItemDragHelperCallBack(new ItemDragHelperCallBack.OnItemDragListener() {
            @Override
            public void onItemMove(int startPos, int endPos) {
                Collections.swap(list,startPos,endPos);
                adapter.notifyItemMoved(startPos,endPos);
            }

            @Override
            public void onItemMoveStartAndEnd(RecyclerView.ViewHolder viewHolder, int status) {
                Log.e(TAG, "viewHolder: "+viewHolder );
                if (status != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setScaleX(1.1f);
                    viewHolder.itemView.setScaleY(1.1f);
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.main_color,null));
                    // 获取系统震动服务
                    Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                    vib.vibrate(150);
                    return;
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 完成移动，选中的改变样式
                viewHolder.itemView.setScaleX(1f);
                viewHolder.itemView.setScaleY(1f);
                viewHolder.itemView.setBackground(null);
            }
        });

        // 增加长按拖动功能
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBack);
        // 关联RecyclerView
        itemTouchHelper.attachToRecyclerView(rec_list);
    }
}
