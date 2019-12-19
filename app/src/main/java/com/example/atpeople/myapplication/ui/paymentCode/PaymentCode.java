package com.example.atpeople.myapplication.ui.paymentCode;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;
import com.example.atpeople.myapplication.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by peng on 2019/12/19
 */
public class PaymentCode extends BaseActivity implements PayPwdView.InputCallBack {


    @BindView(R.id.btn_pay)
    TextView btn_pay;
    PayFragment fragment;

    @Override
    protected int initLayout() {
        return R.layout.activity_payment_code;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
    @OnClick({R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:
                Bundle bundle = new Bundle();
                bundle.putString(PayFragment.EXTRA_CONTENT, "红包:¥ " + 100.00);

                fragment= new PayFragment();
                fragment.setArguments(bundle);
                fragment.setPaySuccessCallBack(PaymentCode.this);
                fragment.show(getSupportFragmentManager(), "Pay");
                break;
        }
    }

    @Override
    public void onInputFinish(String result) {
        fragment.dismiss();
        showToast("输入的支付密码=="+result);
    }
}
