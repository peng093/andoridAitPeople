package com.example.atpeople.myapplication.ui.paymentCode;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;
import com.example.atpeople.myapplication.main.MainActivity;
import com.yasic.library.particletextview.MovingStrategy.CornerStrategy;
import com.yasic.library.particletextview.MovingStrategy.RandomMovingStrategy;
import com.yasic.library.particletextview.Object.ParticleTextViewConfig;
import com.example.atpeople.myapplication.customview.ParticleTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by peng on 2019/12/19
 */
public class PaymentCode extends BaseActivity implements PayPwdView.InputCallBack {


    @BindView(R.id.btn_pay)
    TextView btn_pay;

    PayFragment fragment;
    ParticleTextView particleTextView;

    @Override
    protected int initLayout() {
        return R.layout.activity_payment_code;
    }

    @Override
    protected void initView() {
        particleTextView= (ParticleTextView) findViewById(R.id.particleTextView);
        CornerStrategy randomMovingStrategy = new CornerStrategy();
        ParticleTextViewConfig config = new ParticleTextViewConfig.Builder()
                .setRowStep(8)
                .setColumnStep(8)
                .setTargetText("FMS")
                .setReleasing(0.08)
                .setParticleRadius(4)
                .setMiniDistance(0.1)
                .setTextSize(150)
                // 设置粒子移动轨迹策略
                .setMovingStrategy(randomMovingStrategy)
                // 设置循环动画间的间隔时间
                .instance();
        particleTextView.setConfig(config);

    }

    @Override
    protected void initData() {

    }
    @OnClick({R.id.btn_pay,R.id.btn_pay2})
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
            case R.id.btn_pay2:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        particleTextView.startAnimation();
                        particleTextView.refresh();
                    }
                });
                break;
        }
    }

    @Override
    public void onInputFinish(String result) {
        fragment.dismiss();
        showToast("输入的支付密码=="+result);
    }
}
