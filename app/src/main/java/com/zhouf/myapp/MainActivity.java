package com.zhouf.myapp;

import android.content.Intent;
import android.os.Bundle;
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
    Integer[] items;

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

        num1.addTextChangedListener(new MyWatcher(num1));
        num2.addTextChangedListener(new MyWatcher(num2));
        num3.addTextChangedListener(new MyWatcher(num3));
        num4.addTextChangedListener(new MyWatcher(num4));
        num5.addTextChangedListener(new MyWatcher(num5));
        num6.addTextChangedListener(new MyWatcher(num6));

        //for test
        num1.setText("01,02,03,04,05");
        num2.setText("03,04,05,06");
        num3.setText("06,07,08,15");
        num4.setText("21,22,23,30");
        num5.setText("25");

        List<Integer> list = new ArrayList<Integer>();
        for (int i=0;i<=MAX;i++){
            //list.add(String.format("%02d",i));
            list.add(i);
            Log.d(TAG, "onCreate: 00->" + String.format("%02d",i));
        }
        items = list.toArray(new Integer[]{});

    }

    public void textClick(View text) {
        Log.i(TAG, "textClick: ");


    }

    
    public void onStartClick(View btn){
        Log.i(TAG, "onStartClick: ");

        Integer[] a1,a2,a3,a4,a5,a6;

        a1 = getArray(num1);
        a2 = getArray(num2);
        a3 = getArray(num3);
        a4 = getArray(num4);
        a5 = getArray(num5);
        a6 = getArray(num6);
        int counter = 1;

        ArrayList<String> listData = new ArrayList<>();
        for(int i1 : a1){
            for(int i2 : a2){
                if(i1<i2)
                for(int i3 : a3){
                    if(i2<i3)
                    for(int i4 : a4){
                        if(i3<i4)
                        for(int i5 : a5){
                            if(i4<i5)
                            for(int i6 : a6){
                                if(i5<i6){
                                    Log.i(TAG, "["+(counter)+"]i1~i6=" + i1 + "," + i2 + "," + i3 + "," + i4 + "," + i5 + "," + i6);
                                    StringBuilder sb = new StringBuilder();
                                    //sb.append("[").append(counter).append("] ");
                                    sb.append(String.format("%02d",i1)).append(" - ");
                                    sb.append(String.format("%02d",i2)).append(" - ");
                                    sb.append(String.format("%02d",i3)).append(" - ");
                                    sb.append(String.format("%02d",i4)).append(" - ");
                                    sb.append(String.format("%02d",i5)).append(" - ");
                                    sb.append(String.format("%02d",i6));
                                    listData.add(sb.toString());
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


    private Integer[] getArray(EditText text){
        Integer array[];
        if(text.getText().toString().length()==0){
            array = items;
        }else{
            List<Integer> lst = new ArrayList<>();
            for(String s : text.getText().toString().split(",")){
                lst.add(Integer.valueOf(s));
            }
            array = lst.toArray(new Integer[]{});
        }
        return array;
    }
}
