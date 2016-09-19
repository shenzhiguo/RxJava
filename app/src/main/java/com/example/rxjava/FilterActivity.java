package com.example.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.tv_sequence)
    TextView mSequenceTv;

    @BindView(R.id.lv_filter_result)
    ListView mResultLv;

    @BindString(R.string.result)
    String result;

    private Observable<Integer> observable;
    private Action1<Integer> showAction;
    private Action1<Integer> action;

    private ArrayAdapter<Integer> mFilterAdapter;
    private List<Integer> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        observable = Observable.range(0, 10);
        mFilterAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1,mList);
        mResultLv.setAdapter(mFilterAdapter);

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", 200);
        mSequenceTv.setText("");
        normal();
        mList.clear();
        switch (mode){
            case BaseEntity.EXT_FILTER_MODE:
                filter();
                break;
            case BaseEntity.TAKE_MODE:
                take();
                break;
            case BaseEntity.TAKE_LAST_MODE:
                takeLast();
                break;
            case BaseEntity.DISTINCT_MODE:
                distinct();
                break;
            case BaseEntity.FIRST_MODE:
                first();
                break;
            case BaseEntity.LAST_MODE:
                last();
                break;
            case BaseEntity.SKIP_MODE:
                skip();
                break;
            case BaseEntity.SKIP_LAST_MODE:
                skipLast();
                break;
            case BaseEntity.ELEMENT_AT_MODE:
                elementAt();
                break;
            case BaseEntity.SAMPLING_MODE:
                sample();
                break;
            case BaseEntity.TIMEOUT_MODE:
                timeout();
                break;
            case BaseEntity.DEBOUNCE:
                debounce();
                break;
            case BaseEntity.MERGE_MODE:
                merge();
                break;
            case BaseEntity.ZIP_MODE:
                zip();
                break;
            case BaseEntity.JOIN_MODE:
                join();
                break;

        }
    }

    private void normal(){
        showAction = new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if(mSequenceTv.getText().toString()!=null && !mSequenceTv.getText().toString().isEmpty()){
                    mSequenceTv.setText(mSequenceTv.getText().toString()+"、"+integer);
                } else {
                    mSequenceTv.setText(integer+"");
                }
            }
        };
        observable.subscribe(showAction);

        action = new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mList.add(integer);
                mFilterAdapter.notifyDataSetChanged();
            }
        };
    }

    /* 符合条件的数据项 */
    private void filter(){
        observable.filter(new Func1<Integer,Boolean>() {

            @Override
            public Boolean call(Integer i) {
                return i%2==0;
            }
        }).subscribe(action);
    }

    /* 最前N项数据 */
    private void take(){
        observable.take(3).subscribe(action);
    }

    /* 最后N项数据 */
    private void takeLast(){
        observable.takeLast(3).subscribe(action);
    }

    /* 过滤掉重复的数据项 */
    private void distinct(){
        Observable<Integer> ob = Observable.just(1,1,2,2,3,4,4,1,1,5);
        mSequenceTv.setText("");
        ob.subscribe(showAction);
        ob.distinct().subscribe(action);
    }

    private void first(){
        observable.first().subscribe(action);
    }

    private void last(){
        observable.last().subscribe(action);
    }

    private void skip(){
        observable.skip(2).subscribe(action);
    }

    private void skipLast(){
        observable.skipLast(2).subscribe(action);
    }

    /* 从0 开始 */
    private void elementAt(){
        observable.elementAt(5).subscribe(action);
    }

    /* 每到一个时刻发送该视口获得的数据 */
    private void sample(){
        Observable<Integer> ob = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                    sleep(200);
                }
            }
        });
        mSequenceTv.setText("");
        ob.subscribe(showAction);
        ob.subscribeOn(Schedulers.newThread())
                .sample(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

    /* 超过一定时长没有发射数据报错 */
    private void timeout(){
        Observable.<Integer>never().timeout(1, TimeUnit.SECONDS).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Toast.makeText(FilterActivity.this, "On Completed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(FilterActivity.this, "On Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer integer) {
                mList.add(integer);
                mFilterAdapter.notifyDataSetChanged();
            }
        });
    }

    /* 定时发送数据，时区内发送无效 */
    private void debounce(){
        Observable<Integer> ob = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                sleep(500);
                subscriber.onNext(2);
                sleep(1000);
                subscriber.onNext(3);
                sleep(2000);
                subscriber.onNext(4);
                subscriber.onCompleted();
            }
        });

        ob.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mSequenceTv.setText("1、2、3、4");
            }
        });
        ob.subscribeOn(Schedulers.newThread())
            .debounce(1,TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action);
    }

    private void merge(){
        Observable<Integer> ob1 = Observable.just(1,2,3);
        Observable<Integer> ob2 = Observable.just(4,5,6);

        mSequenceTv.setText("");
        ob1.subscribe(showAction);
        ob2.subscribe(showAction);

        ob1.mergeWith(ob2).subscribe(action);
    }
    
    private void zip(){
        Observable<Integer> ob1 = Observable.just(1,2,3);
        Observable<Integer> ob2 = Observable.just(11, 22, 33);

        mSequenceTv.setText("");
        ob1.subscribe(showAction);
        ob2.subscribe(showAction);


        ob1.zipWith(ob2, new Func2<Integer, Integer, Integer>() {

                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer+integer2*3;
                    }
                }).subscribe(action);
    }

    private void join(){
        Observable<Integer> right = Observable.just(3);
        Func1<Integer,Observable<Integer>> ld = new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                return Observable.just(integer*2);
            }
        };
        Func1<Integer,Observable<Integer>> rd = new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                return Observable.just(integer*3);
            }
        };
        Func2<Integer,Integer,Integer> result = new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer*integer2;
            }
        };
        Observable.just(2).join(right,ld,rd,result).subscribe(action);
    }

    private void sleep(long times){
        try{
            Thread.sleep(times);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
