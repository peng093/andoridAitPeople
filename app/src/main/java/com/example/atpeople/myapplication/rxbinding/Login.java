package com.example.atpeople.myapplication.rxbinding;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.customview.CustomVideoView;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Create by peng on 2019/8/16
 */
public class Login extends AppCompatActivity {
    @BindView(R.id.welcome_videoview)
    CustomVideoView welcome_videoview;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn1)
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            // 状态栏白色字体
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        welcome_videoview.setVideoURI(Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.kr36));
        welcome_videoview.start();
        welcome_videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                welcome_videoview.start();

            }
        });
        rxEditText();
    }
    private void rxEditText() {
        Observable.combineLatest(RxTextView.textChanges(et_name).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return String.valueOf(charSequence);
            }
        }), RxTextView.textChanges(et_password).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return String.valueOf(charSequence);
            }
        }), new BiFunction<String, String, Boolean>() {
            @Override
            public Boolean apply(String name, String password) throws Exception {
                return isNameValid(name) && isPwdValid(password);
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    btn1.setEnabled(true);
                    RxView.clicks(btn1).subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Toast.makeText(Login.this, "Login Success!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    btn1.setEnabled(false);
                }
            }
        });
    }
    /**
     * @Author Peng
     * @Date 2019/8/16 11:47
     * @Describe 这里需要验证是不是手机，或者符合其他规范
     */
    private boolean isNameValid(String name) {
        return "123".equals(name);
    }
    /**
     * @Author Peng
     * @Date 2019/8/16 11:48
     * @Describe 密码验证长度即可
     */
    private boolean isPwdValid(String pwd) {
        return "123".equals(pwd);
    }
}
