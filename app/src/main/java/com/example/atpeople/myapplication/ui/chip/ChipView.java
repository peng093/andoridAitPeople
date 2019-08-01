package com.example.atpeople.myapplication.ui.chip;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;

/**
 * Create by peng on 2019/7/24
 */
public class ChipView extends AppCompatActivity {

    ChipGroup chipGroup2;
    ChipGroup chipInGroup3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chip_view);
        chipGroup2=findViewById(R.id.chipGroup2);
        chipInGroup3=findViewById(R.id.chipGroup3);
        init();
    }

    private void init() {
        chipGroup2.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, @IdRes int checkedId) {
                String hintStr=" ";
                switch(checkedId){
                    case R.id.chipInGroup1:
                        hintStr = "被选中的是 chipInGroup1 ";
                        break;
                    case R.id.chipInGroup2 :
                        hintStr = "被选中的是 chipInGroup2 ";
                        break;
                    case R.id.chipInGroup3:
                        hintStr = "被选中的是 chipInGroup3 ";
                        break;
                    default:
                        hintStr = "没有选中任何chip";
                        break;
                }
                Toast.makeText(ChipView.this, hintStr, Toast.LENGTH_SHORT).show();
            }
        });

        chipInGroup3.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(ChipGroup chipGroup, @IdRes int checkedId) {
                String hintStr=" ";
                switch(checkedId){
                    case R.id.chipInGroup2_1:
                        hintStr = "被选中的是 chipInGroup2_1 ";
                        break;
                    case R.id.chipInGroup2_2 :
                        hintStr = "被选中的是 chipInGroup2_2 ";
                        break;
                    case R.id.chipInGroup2_3:
                        hintStr = "被选中的是 chipInGroup2_3 ";
                        break;
                    default:
                        hintStr = "没有选中任何chip";
                        break;
                }
            }
        });
    }
}
