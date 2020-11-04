package com.example.atpeople.myapplication.ui;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.ui.notify.model.NotifyBean;

import java.util.List;

/**
 * Create by peng on 2020/10/29
 */
public class FilesListAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {
    public FilesListAdapter(@Nullable List<FileBean> data) {
        super(R.layout.adapter_file_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileBean item) {
        View line=helper.getView(R.id.line);
        helper.setText(R.id.tv_name,item.getName())
                .setText(R.id.tv_time,item.getLastModifiedTime()+"-"+item.getSize());
        // 最后一条不显示下划线
        line.setVisibility((getData().size()-1)==helper.getLayoutPosition()?View.GONE:View.VISIBLE);
    }
}
