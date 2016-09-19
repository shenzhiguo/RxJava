package com.example.rxjava;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shenzg0418 on 2016/9/18
 */
public class ObservablesAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> mList;
    private int mTopMode;
    private int mActualMode;

    ObservablesAdapter(Context context, ArrayList<String> list, int topMode){
        mContext = context;
        mList = list;
        mTopMode = topMode;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final HolderView holderView;
        if(view==null){
            holderView = new HolderView();
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_basic_text,null);
            holderView.textView = (TextView) view.findViewById(R.id.tv_basic);
            view.setTag(holderView);
        }else {
            holderView = (HolderView) view.getTag();
        }

        holderView.textView.setText(mList.get(i));
        holderView.textView.setTag(i);

        holderView.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode(mTopMode,(int)holderView.textView.getTag());
                switch (mTopMode) {
                    case BaseEntity.BASIC_MODE:
                        startBasicActivity(mActualMode);
                        break;
                    case BaseEntity.FILTER_MODE:
                        break;
                }
            }
        });

        return view;
    }

    public void startBasicActivity(int mode){
        Intent intent = new Intent(mContext,BasicActivity.class);
        intent.putExtra("mode", mode);
        mContext.startActivity(intent);
    }

    private void switchMode(int topMode,int position) {
        if(topMode==BaseEntity.BASIC_MODE){
            switch (position){
                case 0:
                    mActualMode = BaseEntity.EXT_MODE;
                    break;
                case 1:
                    mActualMode = BaseEntity.JUST_MODE;
                    break;
                case 2:
                    mActualMode = BaseEntity.FROM_MODE;
                    break;
                case 3:
                    mActualMode = BaseEntity.DEFER_MODE;
                    break;
                case 4:
                    mActualMode = BaseEntity.MAP_MODE;
                    break;
                case 5:
                    mActualMode = BaseEntity.FLAT_MAP_MODE;
                    break;
                default:
                    break;
            }
        }else{

        }
    }

    class HolderView {
        public TextView textView;
    }
}
