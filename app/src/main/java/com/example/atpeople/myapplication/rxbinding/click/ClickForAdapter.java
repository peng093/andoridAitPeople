package com.example.atpeople.myapplication.rxbinding.click;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.jakewharton.rxbinding3.widget.RxAdapterView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Create by peng on 2019/8/16
 */
public class ClickForAdapter extends AppCompatActivity {
    @BindView(R.id.ryl_view)
    ListView ryl_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_click);
        ButterKnife.bind(this);
        rxList();
    }

    private void rxList() {
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if(i%2 != 0){
                datas.add("ListView点击 " + i);
            }else {
                datas.add("不支持RecyclerView的点击事件 " + i);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, datas);
        ryl_view.setAdapter(adapter);
        RxAdapterView.itemClicks(ryl_view).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Toast.makeText(ClickForAdapter.this, "List Item Clicked, Position = " + integer, Toast.LENGTH_LONG).show();
            }
        });
    }
}
