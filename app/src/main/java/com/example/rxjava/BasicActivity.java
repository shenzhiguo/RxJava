package com.example.rxjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

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

    private Context mContext;
    private List<Integer> mList = new ArrayList<>();
    private List<Long> mTimeList = new ArrayList<>();
    private ArrayAdapter<Integer> mArrayAdapter;
    private ArrayAdapter<Long> mTimerAdapter;

    private int mode;

    private int a = 2;
    private int b = 3;
    private int c ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);

        Intent mIntent = getIntent();
        mode = Integer.valueOf(mIntent.getIntExtra("mode",100));
        mContext = BasicActivity.this;

        mArrayAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,mList);
        mInfomationLv.setAdapter(mArrayAdapter);

        switch (mode) {
            case BaseEntity.EXT_MODE:
                ext();
                break;
            case BaseEntity.JUST_MODE:
                mMessageEt.setVisibility(View.VISIBLE);
                mSendBtn.setVisibility(View.VISIBLE);
                mResultTv.setVisibility(View.VISIBLE);
                mInfomationLv.setVisibility(View.GONE);
                break;
            case  BaseEntity.FROM_MODE:
                from();
                break;
            case BaseEntity.DEFER_MODE:
                defer();
                break;
            case BaseEntity.MAP_MODE:
                map();
                break;
            case BaseEntity.FLAT_MAP_MODE:
                flatMap();
                break;
        }
    }

    @OnClick(R.id.btn_send) void send(){
        just();
    }

    private void ext(){
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {

            public void call(Subscriber<? super Integer> subscriber) {
                // TODO Auto-generated method stub
                subscriber.onNext(a);
            }
        }).map(new Func1<Integer, Integer>() {

            public Integer call(Integer temp) {
                // TODO Auto-generated method stub
                return temp+b;
            }
        });

        Subscriber<Integer> sub = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Toast.makeText(BasicActivity.this, "On Completed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(BasicActivity.this, "On Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer integer) {
                mList.add(integer);
                mArrayAdapter.notifyDataSetChanged();
            }
        };
        observable.subscribe(sub);

        try{
            Thread.sleep(2000);
        } catch (Exception e){
            e.printStackTrace();
        }

        a = 7;
        b = 8;
        observable.subscribe(sub);
    }

    /* 发送一串指定值 */
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
                mResultTv.setText(result+s);
            }
        });
    }

    /* 发送一个可迭代序列 */
    public void from(){
        List<Integer> items = new ArrayList<Integer>();
        items.add(1);
        items.add(10);
        items.add(100);
        items.add(200);

        Observable<Integer> observableString = Observable.from(items);
        Subscription print = observableString.subscribe(new Observer<Integer>() {

            public void onCompleted() {
                Toast.makeText(BasicActivity.this,"Observable completed",Toast.LENGTH_SHORT).show();
            }

            public void onError(Throwable e) {
                Toast.makeText(BasicActivity.this,"Observable error!",Toast.LENGTH_SHORT).show();
            }

            public void onNext(Integer item) {
                mList.add(item);
                mArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    /* 每次订阅都创建新的Observables对象 */
    private void defer(){
        mTimerAdapter = new ArrayAdapter<Long>(mContext,android.R.layout.simple_list_item_1,mTimeList);
        mInfomationLv.setAdapter(mTimerAdapter);
        Observable<Long> now = Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.just(System.currentTimeMillis());
            }
        });
        now.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.i(getLocalClassName(), aLong + "");
                mTimeList.add(aLong);
                mTimerAdapter.notifyDataSetChanged();
            }
        });
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        now.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.i(getLocalClassName(), aLong + "");
                mTimeList.add(aLong);
                mTimerAdapter.notifyDataSetChanged();
            }
        });
    }

    /* 对发射的数据进行变幻 */
    private void map(){
        Observable.range(1,10).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer*2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mList.add(integer);
                mArrayAdapter.notifyDataSetChanged();
            }
        });

    }

    /* 发射数据集合 -> Observables集合*/
    private void flatMap(){
        Observable.range(1,10).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(final Integer integer) {
                return Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(integer);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread());
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mList.add(integer);
                mArrayAdapter.notifyDataSetChanged();
            }
        });
    }

}
