package com.example.atpeople.myapplication.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.callback.ItemDragHelperCallBack;
import com.example.atpeople.myapplication.ui.notify.NotifyAdapter;
import com.example.atpeople.myapplication.ui.notify.model.NotifyBean;
import com.example.atpeople.myapplication.util.FilePathUtil;
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
    @BindView(R.id.tv_path)
    TextView tv_path;


    String path="";
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
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);

            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){
                path = uri.getPath();
                tv_path.setText(path);
                Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
                return;
            }
            // 4.4以后
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                path = FilePathUtil.getPath(this, uri);
                tv_path.setText(path);
                Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
            } else {
                // 4.4以下下系统调用方法
                path = FilePathUtil.getRealPathFromURI(uri,this);
                tv_path.setText(path);
                Toast.makeText(ListDrag.this, path+"222222", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
