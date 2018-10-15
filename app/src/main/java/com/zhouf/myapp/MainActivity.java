package com.zhouf.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

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


        for(int i=0;i<3;i++)
            select[i].setSelection(1);


        saved = findViewById(R.id.chk_saved);


        //填入保存数据
        SharedPreferences sp = getSharedPreferences("mydata", Activity.MODE_PRIVATE);

        //*

        for(int i=0;i<3;i++) {
            String str = sp.getString("str"+i, "");
            Log.i(TAG, "onCreate: str" + i + "=" + str);
            if(str.length()>0){
                num[i].setText(str);
            }

            int position = sp.getInt("position" + i,1);
            select[i].setSelection(position);
        }

        /*/

        num[0].setText("1 2 3 5 9");
        num[1].setText("13 14 16");
        num[2].setText("22 25 28 31");
        //*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.config_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.config){
            //进入配置页面
            Intent settingIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingIntent);
        }
        return true;
    }

    public void onStartClick(View btn){
        Log.i(TAG, "onStartClick: ");


        //检查输入数据是否合法
        String regex = "[0-9|, ]+";
        for(int i=0;i<3;i++) {
            String str = num[i].getText().toString();
            if(str.length()>0 && !str.matches(regex)){
                //不满足条件
                Toast.makeText(this, "第"+(i+1)+"位输入不合法，请检查", Toast.LENGTH_LONG).show();
                return;
            }
        }

        //是否保存数据
        saveData();

        ArrayList<String> lista = getArrayList(num[0],select[0]);
        ArrayList<String> listb = getArrayList(num[1],select[1]);
        ArrayList<String> listc = getArrayList(num[2],select[2]);

        ArrayList<String> listData = new ArrayList<>();
        for(String stra : lista){
            for(String strb : listb){
                for(String strc : listc){
                    StringBuilder builder = new StringBuilder();
                    builder.append(stra).append(strb).append(strc);
                    builder.deleteCharAt(builder.lastIndexOf(","));
                    listData.add(builder.toString());
                }
            }
        }

        Log.i(TAG, "onStartClick: list.length=" + listData.size());
        //NumUtil.filterData(listData,dan,suang,zhi,he,eqType);

        Log.i(TAG, "onStartClick: list.length2=" + listData.size());

        if(listData.size()==0){
            Toast.makeText(this, R.string.no_data_tip, Toast.LENGTH_LONG).show();
        }else {
            Intent listIntent = new Intent(this, MyListActivity.class);
            listIntent.putStringArrayListExtra("data", listData);
            startActivity(listIntent);
        }
    }


    /**
     * 返回列表集合
     * @param editText 输入的文本控件
     * @param spinner 下拉列表，选用多少个数据
     * @return 返回从文本中自由组合的符合下拉列表个数的集合
     */
    private ArrayList<String> getArrayList(EditText editText, Spinner spinner) {
        //ArrayList<Integer> retList = new ArrayList<Integer>();
        String str = editText.getText().toString();
        int limit = spinner.getSelectedItemPosition();  //0,1,2
        ArrayList<String> resultList = NumUtil.getList(str,limit+1);

        return resultList;
    }

    private void saveData() {
        SharedPreferences mySharedPreferences = getSharedPreferences("mydata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        if(saved.isChecked()){
            Log.i(TAG, "onStartClick: 保存数据");
            editor.clear();
            for(int i=0;i<3;i++) {
                editor.putString("str"+i,num[i].getText().toString());
                editor.putInt("position" + i ,select[i].getSelectedItemPosition());
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

}
