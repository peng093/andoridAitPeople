package com.example.atpeople.myapplication.atPeople;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atPeople.model.Topic;
import com.example.atpeople.myapplication.atPeople.model.User;
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
            List<Topic> data = topicAdapter.getData();
            Topic topic = data.get(position);
            setResult(topic);
        });
    }

    @Override
    protected void initData() {
        List<Topic> result = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Topic topic = new Topic(i, "topic"+i);
            result.add(topic);
        }
        topicAdapter.setNewData(result);
    }

    public class TopicAdapter extends BaseQuickAdapter<Topic, BaseViewHolder> {
        public TopicAdapter(@Nullable List<Topic> data) {
            super(R.layout.user_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Topic item) {
            helper.setText(R.id.user_name,item.getTopic());
        }
    }

    private void setResult(Topic topic) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_TOPIC, topic);
        setResult(RESULT_OK, intent);
        finish();
    }
    public static Intent getTopicListIntent(Activity activity) {
        return new Intent(activity, TopicList.class);
    }
}
