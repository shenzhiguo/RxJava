package com.example.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectActivity extends AppCompatActivity {

    @BindView(R.id.gv_basic)
    GridView mBasicGv;

    @BindView(R.id.gv_filter)
    GridView mFilterGv;

    @BindView(R.id.btn_scheduler)
    Button mSchedulerBtn;

    @BindArray(R.array.basic_functions)
    String[] funcitons;

    @BindArray(R.array.filter_observables)
    String[] filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ObservablesAdapter basicAdapter = new ObservablesAdapter(SelectActivity.this,ArrayToList(funcitons),BaseEntity.BASIC_MODE);

        mBasicGv.setAdapter(basicAdapter);
        mFilterGv.setAdapter(new ObservablesAdapter(SelectActivity.this,ArrayToList(filters),BaseEntity.FILTER_MODE));

        mSchedulerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ArrayList<String> ArrayToList(String[] arrays){
        ArrayList<String> list = new ArrayList<>();
        for(int i=0;i<arrays.length;i++){
            list.add(arrays[i]);
        }
        return list;
    }
}
