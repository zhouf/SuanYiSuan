package com.zhouf.myapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by Lenovo on 2018-4-5.
 */

public class MyWatcher implements TextWatcher {

    private final static String TAG = "Watcher";

    private String beforeText;
    private EditText text;

    public MyWatcher(EditText text){
        this.text = text;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        beforeText = charSequence.toString();
        Log.i(TAG, "beforeTextChanged: beforeText1=" + beforeText);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        String str = charSequence.toString();
        Log.i(TAG, "onTextChanged: start=" + start);
        Log.i(TAG, "onTextChanged: before=" + before);
        Log.i(TAG, "onTextChanged: count=" + count);

        if(!str.matches("[0-9|,]+")){
            //如果不合法，则使用之前的内容
            text.setText(beforeText);
            text.setSelection(0);
            text.setSelection(beforeText.length()-1);//???-1
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
