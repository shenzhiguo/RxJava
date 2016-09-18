package com.example.rxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class BasicActivity extends AppCompatActivity {

    @BindView(R.id.et_message)
    EditText mMessageEt;

    @BindView(R.id.btn_send)
    Button mSendBtn;

    @BindView(R.id.tv_result)
    TextView mResultTv;

    @BindView(R.id.lv_information)
    ListView mInfomationLv;

    @BindString(R.string.result)
    String result;

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);

        Intent mIntent = getIntent();
        ;
        /*Log.i(getLocalClassName(), mIntent.getIntExtra("mode",0));*/
        mode = Integer.valueOf(mIntent.getIntExtra("mode",101));
    }

    @OnClick(R.id.btn_send) void send(){
        switch (mode) {
            case BaseEntity.JUST_MODE:
                just();
                break;

        }
    }
    
    private void just(){
        Observable.just(mMessageEt.getText().toString()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Toast.makeText(BasicActivity.this, "On Completed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(BasicActivity.this, "On Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String s) {
                mResultTv.setText(String.format(result,s));
            }
        });
    }


}
