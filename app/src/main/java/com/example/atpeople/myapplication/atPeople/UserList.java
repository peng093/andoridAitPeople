package com.example.atpeople.myapplication.atPeople;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atPeople.model.UserBean;
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
            List<UserBean> data = mUserAdapter.getData();
            UserBean userBean = data.get(position);
            setResult(userBean);
        });
    }

    @Override
    protected void initData() {
        List<UserBean> result = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            UserBean userBean = new UserBean(i, "name"+i,i % 2 == 0 ? "男" : "女");
            result.add(userBean);
        }
        mUserAdapter.setNewData(result);
    }

    public class UserAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder>{
        public UserAdapter(@Nullable List<UserBean> data) {
            super(R.layout.user_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, UserBean item) {
            helper.setText(R.id.user_name,item.getUserName())
                  .setText(R.id.user_sex,item.getUserSex());
        }
    }

    private void setResult(UserBean userBean) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_USER, userBean);
        setResult(RESULT_OK, intent);
        finish();
    }
    public static Intent getUserListIntent(Activity activity) {
        return new Intent(activity, UserList.class);
    }
}
