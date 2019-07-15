package com.example.atpeople.myapplication.slidinguppanel.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;

import java.util.List;

/**
 * Create by peng on 2019/7/15
 */
public class DialogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public DialogAdapter(@Nullable List<String> data, Context context) {
        super(R.layout.upload_adapter_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv_name=helper.getView(R.id.tv_name);
        tv_name.setText(item);
    }
}
