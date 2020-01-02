package com.example.atpeople.myapplication.ui.table;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by peng on 2019/9/18
 */
public class Table extends BaseActivity {
    private static final String TAG = "Table";
    @BindView(R.id.security)
    TextView security;
    @BindView(R.id.helpFeedback)
    TextView helpFeedback;
    @BindView(R.id.novice)
    TextView novice;
    @BindView(R.id.exitLogin)
    TextView exitLogin;
    @BindView(R.id.tv_test)
    TextView tv_test;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.ui_table;
    }

    @Override
    protected void initView() {
        // 隐藏通用的标题，用于特殊页面，自定义标题
        setToolbarShow(false);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.security,R.id.helpFeedback,R.id.novice,R.id.exitLogin,R.id.tv_test})
    public void onViewClicked(View view) {
        Log.e(TAG, "onViewClicked: "+55555 );
        switch (view.getId()){
            case R.id.security:
                AnnoationTest annotation = new AnnoationTest();
                annotation.setName("abcefg99");
                annotation.setPasdword("123456789011");
                try {
                    checkVariable(annotation);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.helpFeedback:
                NoBug testobj = new NoBug();
                checkMethod(testobj);
                break;
            case R.id.novice:
                break;
            case R.id.exitLogin:
                break;
            case R.id.tv_test:
                break;

        }
    }
    /**
     * @Author Peng
     * @Date 2020/1/2 15:20
     * @Describe 校验对象中的变量值是否在注解范围内
     */
    public static void checkVariable(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        // 测试变量是否符合规范
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields){
            // 获取属性上的@Test注解
            Peng test = field.getAnnotation(Peng.class);
            if(test != null){
                // 设置属性可访问
                field.setAccessible(true);
                // 字符串类型的才判断长度
                if("class java.lang.String".equals(field.getGenericType().toString())){
                    String value = (String)field.get(obj);
                    if(value != null && ((value.length() > test.max()) || value.length() < test.min())){
                        System.out.println(test.description());
                    }
                }
            }
        }

    }
    /**
     * @Author Peng
     * @Date 2020/1/2 15:21
     * @Describe 校验有注解的方法是否有bug
     */
    public static void checkMethod(Object obj) {
        Class<?> clazz = obj.getClass();
        // 获取类中的方法
        Method[] method = clazz.getDeclaredMethods();
        // 用来记录测试产生的 log 信息
        StringBuilder log = new StringBuilder();
        // 记录异常的次数
        int errornum = 0;
        for ( Method m: method ) {
            // 只有被 @Jiancha 标注过的方法才进行测试
            if ( m.isAnnotationPresent( JianCha.class )) {
                try {
                    m.setAccessible(true);
                    m.invoke(obj, null);
                } catch (Exception e) {
                    errornum++;
                    log.append(m.getName());
                    log.append(" ");
                    log.append("has error:");
                    log.append("\n\r  caused by ");
                    //记录测试过程中，发生的异常的名称
                    log.append(e.getCause().getClass().getSimpleName());
                    log.append("\n\r");
                    //记录测试过程中，发生的异常的具体信息
                    log.append(e.getCause().getMessage());
                    log.append("\n\r");
                }
            }
        }
        log.append(clazz.getSimpleName());
        log.append(" has  ");
        log.append(errornum);
        log.append(" error.");
        // 生成测试报告
        System.out.println(log.toString());
    }
}
