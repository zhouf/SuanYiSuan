package com.zhouf.myapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ArrayList<String> data = getIntent().getStringArrayListExtra("data");

        final MyListAdapter adapter = new MyListAdapter(MyListActivity.this,R.layout.list_item,data);
        setListAdapter(adapter);
        /*
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                data.remove(i);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        */

        Toast.makeText(this, "共有"+data.size()+"条记录", Toast.LENGTH_LONG).show();
    }
}
