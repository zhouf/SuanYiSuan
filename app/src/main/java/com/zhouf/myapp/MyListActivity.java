package com.zhouf.myapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.List;

public class MyListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> data = getIntent().getStringArrayListExtra("data");

        ListAdapter adapter = new ArrayAdapter<>(MyListActivity.this,R.layout.list_item,data);
        setListAdapter(adapter);

        Toast.makeText(this, "共有"+data.size()+"条记录", Toast.LENGTH_LONG).show();
    }
}
