package com.example.rxjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shenzg0418 on 2016/9/18.
 */
public class ObservablesAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> mList;

    ObservablesAdapter(Context context, ArrayList<String> list){
        mContext = context;
        mList = list;
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
        HolderView holderView;
        if(view==null){
            holderView = new HolderView();
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_basic_text,null);
            holderView.textView = (TextView) view.findViewById(R.id.tv_basic);
            view.setTag(holderView);
        }else {
            holderView = (HolderView) view.getTag();
        }

        holderView.textView.setText(mList.get(i));

        return view;
    }

    class HolderView {
        public TextView textView;
    }
}
