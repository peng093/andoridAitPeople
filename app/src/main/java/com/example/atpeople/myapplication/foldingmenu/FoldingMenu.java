package com.example.atpeople.myapplication.foldingmenu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.foldingmenu.adapter.FoldAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create by peng on 2019/7/18
 */
public class FoldingMenu extends AppCompatActivity {
    private static final String TAG = "FoldingMenu";
    private String TAG_ARROW = "service_arrow";
    private String TAG_BOTTOM = "bottom_show";

    private LinearLayout mTrafficLayout;
    private RelativeLayout mTrafficBarLayout;
    private FoldingLayout mTrafficFoldingLayout;

    RecyclerView recycle;
    FoldAdapter foldAdapter;

    String[] city={"北京","上海","深圳大叔大婶","广州","sdfasdssdfsfdfsdgs胜多负少的胜多负少的gsdsvbxcxcxv","清淡","长沙","程度","是的撒","asdad胜多负少的长度 "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fold_menu);
        mTrafficLayout = (LinearLayout) findViewById(R.id.traffic_layout);
        mTrafficBarLayout = (RelativeLayout) findViewById(R.id.traffic_bar_layout);
        mTrafficFoldingLayout = ((FoldingLayout) findViewById(R.id.traffic_item));

        recycle=findViewById(R.id.recycle);
        mTrafficBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnimation(v, mTrafficFoldingLayout, mTrafficLayout, null);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recycle.setLayoutManager(layoutManager);
        foldAdapter=new FoldAdapter(new ArrayList<String>(),this);
        recycle.setAdapter(foldAdapter);
        foldAdapter.setNewData(Arrays.asList(city));
        foldAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<String>list=adapter.getData();
                Log.e(TAG, "onItemClick: "+ list.get(position));
                Toast.makeText(FoldingMenu.this,list.get(position),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void handleAnimation(final View bar, final FoldingLayout foldingLayout, View parent, final View nextParent) {
        final ImageView arrow = parent.findViewWithTag(TAG_ARROW);
        final LinearLayout lly_show_bottom = parent.findViewWithTag(TAG_BOTTOM);
        foldingLayout.setFoldListener(new OnFoldListener() {

            @Override
            public void onStartFold(float foldFactor) {
                bar.setClickable(true);
                arrow.setBackgroundResource(R.drawable.service_arrow_up);
                lly_show_bottom.setVisibility(View.GONE);
            }

            @Override
            public void onFoldingState(float foldFactor, float foldDrawHeight) {
                bar.setClickable(false);
            }

            @Override
            public void onEndFold(float foldFactor) {
                bar.setClickable(true);
                arrow.setBackgroundResource(R.drawable.service_arrow_down);
                lly_show_bottom.setVisibility(View.VISIBLE);
            }
        });
        // 折叠
        animateFold(foldingLayout, 500);
    }
    @SuppressLint("NewApi")
    public void animateFold(FoldingLayout foldLayout, int duration) {
        float foldFactor = foldLayout.getFoldFactor();

        ObjectAnimator animator = ObjectAnimator.ofFloat(foldLayout, "foldFactor", foldFactor, foldFactor > 0 ? 0 : 1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(0);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }
}
