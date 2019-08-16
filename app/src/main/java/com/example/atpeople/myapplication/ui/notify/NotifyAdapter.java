package com.example.atpeople.myapplication.ui.notify;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.ui.notify.model.NotifyBean;

import java.util.List;

/**
 * Create by peng on 2019/8/2
 */
public class NotifyAdapter extends BaseQuickAdapter<NotifyBean, BaseViewHolder> {
     Context context;
    public NotifyAdapter(List<NotifyBean> data, Context mt) {
        super(R.layout.item_view, data);
        context=mt;
    }

    @Override
    protected void convert(BaseViewHolder helper, NotifyBean item) {
//        ImageView imageview=helper.getView(R.id.imageview);
//        TextView titleTextview=helper.getView(R.id.title_textview);
//        TextView typeTextview=helper.getView(R.id.type_textview);
//
//        imageview.setImageResource(item.getImageId());
//        titleTextview.setText(item.getTitleId());
//        typeTextview.setText(item.getTypeId());

        helper.setImageResource(R.id.imageview,item.getImageId())
                .setText(R.id.title_textview,item.getTitleId())
                .setText(R.id.type_textview,item.getTypeId());
    }
}
