package com.example.atpeople.myapplication.ui.betterSpinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.example.atpeople.myapplication.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by peng on 2019/8/5
 */
public class Spinner extends AppCompatActivity {
    @BindView(R.id.spinner2)
    MaterialBetterSpinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.better_spinner);
        ButterKnife.bind(this);

        String[] list = getResources().getStringArray(R.array.testString);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, list);
        spinner2.setAdapter(adapter);
    }
}
