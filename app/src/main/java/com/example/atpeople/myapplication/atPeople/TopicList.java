package com.example.atpeople.myapplication.atPeople;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atPeople.model.TopicBean;
import com.example.atpeople.myapplication.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by peng on 2020/11/26
 */
public class TopicList extends BaseActivity {
    @BindView(R.id.recycler)
    RecyclerView recycler;


    public static final String RESULT_TOPIC = "RESULT_TAG";
    private TopicAdapter topicAdapter;

    @Override
    protected int initLayout() {
        return R.layout.common_recycler;
    }

    @Override
    protected void initView() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        topicAdapter = new TopicList.TopicAdapter(new ArrayList<>());
        recycler.setAdapter(topicAdapter);
        topicAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<TopicBean> data = topicAdapter.getData();
            TopicBean topicBean = data.get(position);
            setResult(topicBean);
        });
    }

    @Override
    protected void initData() {
        List<TopicBean> result = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            TopicBean topicBean = new TopicBean(i, "topic"+i);
            result.add(topicBean);
        }
        topicAdapter.setNewData(result);
    }

    public class TopicAdapter extends BaseQuickAdapter<TopicBean, BaseViewHolder> {
        public TopicAdapter(@Nullable List<TopicBean> data) {
            super(R.layout.user_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TopicBean item) {
            helper.setText(R.id.user_name,item.getTopic());
        }
    }

    private void setResult(TopicBean topicBean) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_TOPIC, topicBean);
        setResult(RESULT_OK, intent);
        finish();
    }
    public static Intent getTopicListIntent(Activity activity) {
        return new Intent(activity, TopicList.class);
    }
}
