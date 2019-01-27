package com.zhouf.myapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter {

    private static final String TAG = "MyListAdapter";
    private ArrayList<String> list;

    public MyListAdapter(Context context, int resource, ArrayList<String> list) {
        super(context, resource, list);
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        String stra = (String)getItem(position);
        //stra = 3#1,2,3 第一个是位置
        String s[] = stra.split("#");
        String str = s[1];
        Item item = (Item)getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.listText);
        title.setText(item.toString());

        ImageView img = (ImageView)itemView.findViewById(R.id.itemImg);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                MyListAdapter.this.notifyDataSetChanged();
                Log.i(TAG, "onClick: delete....");
            }
        });

        return itemView;
    }
}
