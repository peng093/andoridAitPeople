package com.example.atpeople.myapplication.atPeople;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atPeople.model.User;
import com.example.atpeople.myapplication.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by peng on 2020/11/13
 */
public class UserList extends BaseActivity {
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private UserAdapter mUserAdapter;
    public static final String RESULT_USER = "RESULT_USER";

    @Override
    protected int initLayout() {
        return R.layout.common_recycler;
    }

    @Override
    protected void initView() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new UserAdapter(new ArrayList<>());
        recycler.setAdapter(mUserAdapter);
        mUserAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<User> data = mUserAdapter.getData();
            User user = data.get(position);
            setResult(user);
        });
    }

    @Override
    protected void initData() {
        List<User> result = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            User user = new User(i, "name"+i,i % 2 == 0 ? "男" : "女");
            result.add(user);
        }
        mUserAdapter.setNewData(result);
    }

    public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder>{
        public UserAdapter(@Nullable List<User> data) {
            super(R.layout.user_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, User item) {
            helper.setText(R.id.user_name,item.getUserName())
                  .setText(R.id.user_sex,item.getUserSex());
        }
    }

    private void setResult(User user) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_USER, user);
        setResult(RESULT_OK, intent);
        finish();
    }
    public static Intent getUserListIntent(Activity activity) {
        return new Intent(activity, UserList.class);
    }
}
