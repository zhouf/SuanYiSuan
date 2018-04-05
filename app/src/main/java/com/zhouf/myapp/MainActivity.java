package com.zhouf.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "Main";

    private final static int MAX = 35;

    EditText num1,num2,num3,num4,num5,num6;
    String[] items = {"aaa","bbbb","cccc"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);
        num6 = findViewById(R.id.num6);

        //for test
        num1.setText("01,02,03,04,05");
        num2.setText("03,04,05,06");
        num3.setText("06,07,08,15");
        num4.setText("21,22,23,30");
        num5.setText("25");

        List<String> list = new ArrayList<String>();
        for (int i=0;i<=MAX;i++){
            list.add(String.format("%02d",i));
            Log.d(TAG, "onCreate: 00->" + String.format("%-2d",i));
        }
        items = list.toArray(new String[]{});

    }

    public void textClick(View text) {
        Log.i(TAG, "textClick: ");


    }

    public void textClick2(View text) {
        Log.i(TAG, "textClick: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请选择数字");
        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which,boolean isChecked) {
                // TODO Auto-generated method stub
                String select_item = items[which].toString();
                //Toast.makeText(MainActivity.this, "选择了--->>" + select_item, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    
    public void onStartClick(View btn){
        Log.i(TAG, "onStartClick: ");

        String[] a1,a2,a3,a4,a5,a6;

        a1 = getArray(num1);
        a2 = getArray(num2);
        a3 = getArray(num3);
        a4 = getArray(num4);
        a5 = getArray(num5);
        a6 = getArray(num6);
        int counter = 1;

        ArrayList<String> listData = new ArrayList<>();
        for(String i1 : a1){
            for(String i2 : a2){
                if(i1.compareTo(i2)<0)
                for(String i3 : a3){
                    if(i2.compareTo(i3)<0)
                    for(String i4 : a4){
                        if(i3.compareTo(i4)<0)
                        for(String i5 : a5){
                            if(i4.compareTo(i5)<0)
                            for(String i6 : a6){
                                if(i5.compareTo(i6)<0){
                                //if(check(i1,i2,i3,i4,i5,i6)){
                                    Log.i(TAG, "["+(counter)+"]i1~i6=" + i1 + "," + i2 + "," + i3 + "," + i4 + "," + i5 + "," + i6);
                                    listData.add(i1 + " - " + i2 + " - " + i3 + " - " + i4 + " - " + i5 + " - " + i6);
                                    counter++;
                                }
                            }
                        }
                    }
                }
            }
        }
        //end for

        Intent listIntent = new Intent(this,MyListActivity.class);
        listIntent.putStringArrayListExtra("data",listData);
        startActivity(listIntent);
    }

    private boolean check(String i1,String i2,String i3,String i4,String i5,String i6){
        return (i1.compareTo(i2)<0 && i2.compareTo(i3)<0 && i3.compareTo(i4)<0 && i4.compareTo(i5)<0 && i5.compareTo(i6)<0);
    }

    private String[] getArray(EditText text){
        String array[];
        if(text.getText().toString().length()==0){
            array = items;
            Log.d(TAG, "getArray: items=" + items);
        }else{
            array = text.getText().toString().split(",");
        }
        return array;
    }
}
