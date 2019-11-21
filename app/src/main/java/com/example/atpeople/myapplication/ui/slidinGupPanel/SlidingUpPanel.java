package com.example.atpeople.myapplication.ui.slidinGupPanel;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.ui.slidinGupPanel.adapter.DialogAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;

/**
 * Create by peng on 2019/7/15
 */
public class SlidingUpPanel extends AppCompatActivity {
    private static final String TAG = "SlidingUpPanel";
    ImageView iv_img;
    RecyclerView recyclerView;
    SlidingUpPanelLayout mLayout;
    Toolbar toolbar;
    Button bt_click_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_up_view);
        iv_img=findViewById(R.id.iv_img);
        recyclerView=findViewById(R.id.recycle);
        mLayout =findViewById(R.id.sliding_layout);
        toolbar =findViewById(R.id.main_toolbar);
        bt_click_show=findViewById(R.id.bt_click_show);
        // toolbar设置标题需要在setsupportActionbar（）的前面才有效。
        toolbar.setTitle("底部上滑面板~");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_click_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        // 滑动卡片监听滑动
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.e(TAG, "onPanelStateChanged " + newState);
                // 关闭状态
                if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                    iv_img.setImageResource(R.mipmap.ic_expand_more_black_48dp);
                }else {
                    iv_img.setImageResource(R.mipmap.ic_expand_less_black_48dp);
                }
            }
        });
        // 监听点击
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(COLLAPSED);
            }
        });

        // 底部列表
        final List<String> datas=new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("测试文字"+i);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DialogAdapter adapter = new DialogAdapter(new ArrayList<String>(),this);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(datas);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(SlidingUpPanel.this,datas.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @Author Peng
     * @Date 2019/7/15 9:57
     * @Describe 点击显示底部面板
     */

    public void showDialog() {
        final List<String>datas=new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("测试文字"+i);
        }
        View contentView = LayoutInflater.from(this)
                .inflate(R.layout.layout_bottom_dialog, null);
        RecyclerView recyclerView= (RecyclerView) contentView.findViewById(R.id.recycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DialogAdapter adapter = new DialogAdapter(new ArrayList<String>(),this);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(datas);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(contentView);

        dialog.show();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<String> strings=adapter.getData();
                Toast.makeText(SlidingUpPanel.this,datas.get(position),Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (mLayout != null) {
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
                item.setTitle(R.string.action_show);
            } else {
                item.setTitle(R.string.action_hide);
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }
            case R.id.action_anchor: {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.7f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        item.setTitle(R.string.action_anchor_disable);
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_anchor_enable);
                    }
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
