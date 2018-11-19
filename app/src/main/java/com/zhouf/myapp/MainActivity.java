package com.zhouf.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private final static String TAG = "MainTAG";

    private final static int MAX = 33;
    private final static int LIMIT = 10000;

    EditText num[] = new EditText[6];
    Spinner select[] = new Spinner[6];
    CheckBox saved;
    int less16;
    boolean eqType;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int numArray[] = {R.id.num1,R.id.num2,R.id.num3,R.id.num4,R.id.num5,R.id.num6};
        int spinnerArray[] = {R.id.spinner1,R.id.spinner2,R.id.spinner3,R.id.spinner4,R.id.spinner5,R.id.spinner6};

        for(int i=0;i<6;i++){

            num[i] = findViewById(numArray[i]);
            select[i] = findViewById(spinnerArray[i]);
        }

        saved = findViewById(R.id.chk_saved);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useConfig = sharedPrefs.getBoolean("use_setting",false);
        boolean useEndwith = sharedPrefs.getBoolean("use_endwith",false);
        String matchType = sharedPrefs.getString("match_type","");
        String endwithStr = endwithDescribe(getEndwithMap());

        //dan = Integer.parseInt(sharedPrefs.getString(getString(R.string.settings_item_dan_key),"0"));
        //zhi = Integer.parseInt(sharedPrefs.getString(getString(R.string.settings_item_zhi_key),"0"));
        less16 = Integer.parseInt(sharedPrefs.getString(getString(R.string.settings_item_less16_key),"0"));
        String sureNum = sharedPrefs.getString("sure_num","");
        Log.i(TAG, "onCreate: useConfig=" + useConfig);

        TextView configTip = findViewById(R.id.config_tip);


        String tipstr = "";
        if(useConfig){
            tipstr = "使用过滤条件，" + matchType + "(1~16:"+less16+")";
        }
        if(sureNum!=null && sureNum.length()>0){
            tipstr = "["+sureNum+"]" + tipstr;
        }
        if(useEndwith && endwithStr!=null && endwithStr.length()>0){
            tipstr = tipstr.length()>6? (tipstr + '\n' + endwithStr) : endwithStr;
        }

        if(tipstr.trim().length()>0){
            configTip.setText(tipstr);
            configTip.setVisibility(View.VISIBLE);
        }else{
            configTip.setVisibility(View.GONE);
        }

        //填入保存数据
        SharedPreferences sp = getSharedPreferences("mydata", Activity.MODE_PRIVATE);
        //*

        for(int i=0;i<6;i++) {
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
        for(int i=0;i<6;i++) {
            String str = num[i].getText().toString();
            if(str.length()>0 && !str.matches(regex)){
                //不满足条件
                Toast.makeText(this, "第"+(i+1)+"位输入不合法，请检查", Toast.LENGTH_LONG).show();
                return;
            }
        }

        //是否保存数据
        saveData();

        btn.setEnabled(false);
        ArrayList<String> listData = new ArrayList<>();
        ArrayList<String> lista = getArrayList(num[0],select[0]);
        for (int i=1;i<6;i++){
            Log.i(TAG, "onStartClick: lista["+lista.size()+"]=" + lista);
            ArrayList<String> listb = getArrayList(num[i],select[i]);
            Log.i(TAG, "onStartClick: listb=" + listb);
            lista = unionList(lista,listb);
            Log.i(TAG, "run: list.size=" + lista.size());
            int datasize = lista.size();
            if(datasize==0 || datasize>LIMIT){
                //当前面的数据不符合条件时，退出循环
                Log.i(TAG, "onStartClick: 退出循环 exit loop datasize=" + datasize);
                break;
            }
        }

        /*boolean useConfig = sharedPrefs.getBoolean("use_setting",false);
        String matchType = sharedPrefs.getString("match_type","");
        eqType = getString(R.string.settings_item_option_equals).equals(matchType);*/

        //过滤尾数设置
        boolean useEndwith = sharedPrefs.getBoolean("use_endwith",false);
        String sureNum = sharedPrefs.getString("sure_num","");
        Map<Integer, Integer> endMap = getEndwithMap();

        for(String stra : lista){
            StringBuilder builder = new StringBuilder(stra);
            builder.deleteCharAt(builder.lastIndexOf(","));
            //sort
            String sortedStr = sortStr(builder.toString(),useEndwith,endMap,sureNum);

            if(!TextUtils.isEmpty(sortedStr)){
                listData.add(sortedStr);
            }
        }


        int datasize = listData.size();
        if(datasize==0) {
            Toast.makeText(this, R.string.no_data_tip, Toast.LENGTH_LONG).show();
        }else if(datasize>LIMIT){
            Toast.makeText(this, R.string.tomany_data_tip, Toast.LENGTH_LONG).show();
        }else {
            Log.i(TAG, "onStartClick: 打开列表窗口");
            Intent listIntent = new Intent(this, MyListActivity.class);
            listIntent.putStringArrayListExtra("data", listData);
            startActivity(listIntent);
        }
        btn.setEnabled(true);
    }

    /**
     * 获得尾号过滤的map信息
     * @return
     */
    private Map<Integer, Integer> getEndwithMap() {
        Map<Integer,Integer> endMap = new HashMap<Integer,Integer>();
        for(int i=0;i<=9;i++){
            //set0~set9
            Integer v = Integer.valueOf(sharedPrefs.getString("set" + i,"0"));
            if(v>0){
                //有设置内容，放入Map
                endMap.put(i,v);
                //Log.i(TAG, "onStartClick: endMap("+i+")->" + v);
            }
        }
        return endMap;
    }

    //返回尾号过滤的文本描述
    private String endwithDescribe(Map<Integer,Integer> map){
        StringBuilder retStr = new StringBuilder();
        for(Integer k : map.keySet()){
            retStr.append(k).append('[').append(map.get(k)).append("个],");
        }
        if(retStr.toString().endsWith(",")){
            retStr.deleteCharAt(retStr.lastIndexOf(","));
        }
        if(retStr.length()>0){
            retStr.insert(0,"尾数限定：");
        }
        return retStr.toString();
    }

    /**
     * 对字串中的数据进行排序
     * @param s 字串如：3，12，6，8，5
     * @param useEndwith 是否启用尾数过滤
     * @param endMap 尾数过滤设置项
     * @param sureNum 一定会出现的数据，可为空，或2,3
     * @return 排序后的字串如：3，5，6，8，12
     */
    private String sortStr(String s,boolean useEndwith,Map<Integer,Integer> endMap,String sureNum) {
        String items[] = s.split(",");
        Integer num[] = new Integer[items.length];
        for (int i = 0; i < items.length; i++) {
            num[i] = Integer.valueOf(items[i]);
        }
        Arrays.sort(num);

        //判断过滤尾数
        if(useEndwith){
            //对endMap中每个规则进行过滤
            for(Integer k : endMap.keySet()){
                //k是尾数规则
                int limit = endMap.get(k);//规则上限

                //统计尾数是k的数据个数
                int cnt = 0;
                for(int n : num){
                    //遍历数据进行判断
                    if(n%10==k){
                        cnt++;
                    }
                }

                if(cnt<limit){
                    //不满足条件，直接返回空
                    return "";
                }
            }
        }

        if(sureNum.trim().length()>0){
            //如果有设置一定出现的数字
            List<Integer> numList = Arrays.asList(num);
            for(String ss : sureNum.split(",")){
                Integer k1 = Integer.valueOf(ss);
                if(!numList.contains(k1)){
                    return "";
                }
            }
        }



        String retStr = Arrays.toString(num);
        retStr = retStr.substring(1, retStr.length()-1).replaceAll(" ", "");
        return retStr;
    }

    /**
     * 用于合并两个集合，对两个集合进行组合
     * @param lista
     * @param listb
     * @return
     */
    private ArrayList<String> unionList(ArrayList<String> lista, ArrayList<String> listb) {
        ArrayList<String> retList = new ArrayList<>();
        int lena = lista.size();
        int lenb = listb.size();

        if(lena==0 && lenb==0){
            return retList;
        }else if(lena==0 && lenb>0){
            return listb;
        }else if(lena>=0 && lenb==0){
            return lista;
        }else{
            //两个集合都包含元素，对符合条件的数据进行判断
            boolean useConfig = sharedPrefs.getBoolean("use_setting",false);
            String matchType = sharedPrefs.getString("match_type","");
            eqType = getString(R.string.settings_item_option_equals).equals(matchType);
            //useConfig = false;
            Log.i(TAG, "unionList: less16=" + less16 + " eqType=" + eqType);

            int addCounter = 0;
            for(String stra : lista){
                for(String strb : listb){
                    if(!(useConfig && !NumUtil.checkOneOK(less16,eqType,stra+strb))){
                        retList.add(stra + strb);
                        addCounter++;
                        if(addCounter>LIMIT){
                            //如果有超过上限，退出
                            return retList;
                        }
                    }else{
                        Log.i(TAG, "unionList: 舍弃");
                    }
                }
            }
        }
        return retList;
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
        if(spinner.getAdapter().getCount()==5){
            //如果是第一组5个元素，从1开始，下标需+1
            limit++;
        }
        Log.i(TAG, "getArrayList: str=" + str + " limit=" + limit);
        ArrayList<String> resultList = NumUtil.getList(str,limit);

        return resultList;
    }

    private void saveData() {
        SharedPreferences mySharedPreferences = getSharedPreferences("mydata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        if(saved.isChecked()){
            Log.i(TAG, "onStartClick: 保存数据");
            editor.clear();
            for(int i=0;i<6;i++) {
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
