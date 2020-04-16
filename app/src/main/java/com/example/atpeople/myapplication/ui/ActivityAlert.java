package com.example.atpeople.myapplication.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.databinding.ActivityAlertBinding;
import com.example.atpeople.myapplication.ui.activityAlert.MainViewModel;
import com.example.atpeople.myapplication.ui.activityAlert.TestBean;

/**
 * Create by peng on 2019/7/25
 */
public class ActivityAlert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAlertBinding binding= DataBindingUtil.setContentView(this, R.layout.activity_alert);
        Bundle b = getIntent().getExtras();
        MainViewModel data = b.getParcelable("data");
        binding.setActivityalert(data);
        binding.setData2(new TestBean("",66666));
        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }
    }
}
