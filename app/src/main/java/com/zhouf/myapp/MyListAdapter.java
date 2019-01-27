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
    private ArrayList<Item> list;

    public MyListAdapter(Context context, int resource, ArrayList<Item> list) {
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

        TextView txtId = (TextView)itemView.findViewById(R.id.textid);
        txtId.setText(String.valueOf(item.getSeq()));

        return itemView;
    }
}
