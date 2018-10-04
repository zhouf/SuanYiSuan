package com.zhouf.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "Main";

    private final static int MAX = 33;

    EditText num[] = new EditText[3];
    Spinner select[] = new Spinner[3];
    CheckBox saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        num[0] = findViewById(R.id.num1);
        num[1] = findViewById(R.id.num2);
        num[2] = findViewById(R.id.num3);

        select[0] = findViewById(R.id.spinner1);
        select[1] = findViewById(R.id.spinner2);
        select[2] = findViewById(R.id.spinner3);

        saved = findViewById(R.id.chk_saved);


        //填入保存数据
        //SharedPreferences sp = getSharedPreferences("mydata", Activity.MODE_PRIVATE);


        /*

        for(int i=0;i<6;i++) {
            String str = sp.getString("str"+i, "");
            Log.i(TAG, "onCreate: str" + i + "=" + str);
            if(str.length()>0){
                num[i].setText(str);
            }
        }

        /*/

        num[0].setText("1,2,3,5");
        num[1].setText("13,14,16");
        num[2].setText("22,25,28,31");
        //*/

    }

    public void onStartClick(View btn){
        Log.i(TAG, "onStartClick: ");
        //检查输入数据是否合法
        String regex = "[0-9|,]+";
        for(int i=0;i<3;i++) {
            String str = num[i].getText().toString();
            if(str.length()>0 && !str.matches(regex)){
                //不满足条件
                Toast.makeText(this, "第"+(i+1)+"位输入不合法，请检查", Toast.LENGTH_LONG).show();
                return;
            }
        }

        
        //是否保存数据
        //saveData();

        Integer[] a1,a2,a3,a4,a5,a6;

        a1 = getArray(num[0]);
        a2 = getArray(num[1]);
        a3 = getArray(num[2]);
        a4 = getArray(num[3]);
        a5 = getArray(num[4]);
        a6 = getArray(num[5]);
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

    private void saveData() {
        SharedPreferences mySharedPreferences = getSharedPreferences("mydata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        if(saved.isChecked()){
            Log.i(TAG, "onStartClick: 保存数据");
            editor.clear();
            for(int i=0;i<6;i++) {
                editor.putString("str"+i,num[i].getText().toString());
            }
            editor.commit();
            Log.i(TAG, "saveData: 数据写入成功");
        }else{
            Log.i(TAG, "onStartClick: 清理保存数据");
            editor.clear();
            editor.commit();
            Log.i(TAG, "saveData: 数据清理完毕");
        }
    }


    private Integer[] getArray(EditText text){

        List<Integer> list = new ArrayList<>();
        if(text.getText().toString().length()==0){
            for (int i=0;i<=MAX;i++){
                list.add(i);
            }
        }else{
            for(String s : text.getText().toString().split(",")){
                list.add(Integer.valueOf(s));
            }
        }
        return list.toArray(new Integer[]{});
    }
}
