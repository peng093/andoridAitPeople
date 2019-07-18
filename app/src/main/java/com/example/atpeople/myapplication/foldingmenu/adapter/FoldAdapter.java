package com.example.atpeople.myapplication.foldingmenu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;

import java.util.List;

/**
 * Create by peng on 2019/7/18
 */
public class FoldAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Context mContext;

    public FoldAdapter(@Nullable List<String> data, Context context) {
        super(R.layout.upload_adapter_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv_name=helper.getView(R.id.tv_name);
        int color = mContext.getResources().getColor(R.color.material_orange_900,null);
        tv_name.setText(item);
        tv_name.setTextColor(color);
    }
}
