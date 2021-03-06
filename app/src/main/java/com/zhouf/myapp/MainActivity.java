package com.zhouf.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity{

    private final static String TAG = "MainTAG";

    private final static int MAX = 33;
    private final static int LIMIT = 10000;
    private final static int RESULT_MAX = 1000; //结果列表最大上限

    EditText[] num = new EditText[6];
    Spinner[] select = new Spinner[6];
    CheckBox saved;
    TextView configTip;
    int less16;
    boolean eqType;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[] numArray = {R.id.num1, R.id.num2, R.id.num3, R.id.num4, R.id.num5, R.id.num6};
        int[] spinnerArray = {R.id.spinner1,R.id.spinner2,R.id.spinner3,R.id.spinner4,R.id.spinner5,R.id.spinner6};

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

        configTip = findViewById(R.id.config_tip);


        String tipstr = "";
        if(useConfig){
            tipstr = "使用过滤条件，" + matchType + "(1~16:"+less16+")";
        }
        if(sureNum.length() > 0){
            tipstr = "["+sureNum+"]" + tipstr;
        }
        if(useEndwith && endwithStr.length() > 0){
            tipstr = tipstr.length()>2? (tipstr + endwithStr) : endwithStr;
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
        ArrayList<Item> listData = new ArrayList<Item>(LIMIT);
        //前三组数放在lista中
        ArrayList<Item> lista = new ArrayList<Item>(100);
        getArrayList(lista,num[0],select[0]);
        for (int i=1;i<3;i++){
            Log.i(TAG, "onStartClick: lista["+lista.size()+"]=" + lista);
            ArrayList<Item> listb = new ArrayList<Item>(30);
            getArrayList(listb,num[i],select[i]);
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
        //后三组数放在listb中
        ArrayList<Item> listb = new ArrayList<Item>(100);
        getArrayList(listb,num[3],select[3]);
        for (int i=4;i<6;i++){
            Log.i(TAG, "onStartClick: listb["+listb.size()+"]=" + listb);
            ArrayList<Item> listc = new ArrayList<Item>(30);
            getArrayList(listc,num[i],select[i]);
            Log.i(TAG, "onStartClick: listc=" + listc);
            listb = unionList(listb,listc);
            Log.i(TAG, "run: list.size=" + listb.size());
            int datasize = listb.size();
            if(datasize==0 || datasize>LIMIT){
                //当前面的数据不符合条件时，退出循环
                Log.i(TAG, "onStartClick: 退出循环 exit loop datasize=" + datasize);
                break;
            }
        }

        /*boolean useConfig = sharedPrefs.getBoolean("use_setting",false);
        String matchType = sharedPrefs.getString("match_type","");
        eqType = getString(R.string.settings_item_option_equals).equals(matchType);*/

        Log.i(TAG, "onStartClick: lista.size=" + lista.size() + " listb.size=" + listb.size());

        byte endLimit = Byte.parseByte(sharedPrefs.getString("end_limit","0"));//尾数个位数限定
        byte oddLimit = Byte.parseByte(sharedPrefs.getString("odd_limit","0"));//单数控制
        //过滤尾数设置
        boolean useEndwith = sharedPrefs.getBoolean("use_endwith",false);
        String sureNum = sharedPrefs.getString("sure_num","");
        Map<Integer, Integer> endMap = getEndwithMap();

        int sizea = lista.size();
        int sizeb = listb.size();
        int cnta=0,cntb,cntc=0;
        for(Item stra : lista){
            cnta++;
            cntb = 0;
            for(Item strb : listb){
                cntb++;
                //sort
                Item item = stra.add(strb);
                boolean isok = sortStr(item,useEndwith,endMap,sureNum,endLimit,oddLimit);

                if(isok){
                    cntc++;
                    item.setSeq(cntc);
                    listData.add(item);
                }
                String runstr = String.format("过滤数据a %d/%d,b %d/%d 有效%d",sizea,cnta,sizeb,cntb,cntc);
                Log.i(TAG, "onStartClick: runstr=" + runstr);
                //configTip.setText(runstr);

                if(cntc>RESULT_MAX){
                    break;
                }
            }

            if(cntc>RESULT_MAX){
                Toast.makeText(this, R.string.result_data_tip, Toast.LENGTH_LONG).show();
                break;
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
            listIntent.putExtra("data", listData);
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
     * @param item 字串如：3，12，6，8，5
     * @param useEndwith 是否启用尾数过滤
     * @param endMap 尾数过滤设置项
     * @param sureNum 一定会出现的数据，可为空，或2,3
     * @return 如果符合条件返回true
     */
    private boolean sortStr(Item item,boolean useEndwith,Map<Integer,Integer> endMap,String sureNum,byte endLimit,byte oddLimit) {
        Byte num[] = item.getData().toArray(new Byte[]{});
        Arrays.sort(num);

        //判断过滤尾数
        if(useEndwith){
            //对endMap中每个规则进行过滤
            for(Integer k : endMap.keySet()){
                //k是尾数规则
                int limit = endMap.get(k);//规则上限

                //统计尾数是k的数据个数
                int cnt = 0;
                for(byte n : num){
                    //遍历数据进行判断
                    if(n%10==k){
                        cnt++;
                    }
                }

                if(cnt<limit){
                    //不满足条件，直接返回空
                    return false;
                }
            }
        }


        if(endLimit>0){
            //如果要进行尾数个位数限定
            Set<Byte> sets = new HashSet<Byte>();
            for(Byte b : num){
                sets.add((byte)(b%10));
            }
            if(sets.size()>endLimit){
                return false;//不符合条件
            }
        }

        if(oddLimit>0){
            //如果要进行奇数个数验证
            byte oddcnt = 0;
            for(byte b : num){
                if(b%2==1){
                    oddcnt++;
                }
            }
            if(oddcnt!=oddLimit){
                return false;//不符合条件
            }
        }

        if(sureNum.trim().length()>0){
            //如果有设置一定出现的数字
            for(String ss : sureNum.split(",")){
                Byte k1 = Byte.valueOf(ss);
                if(!item.getData().contains(k1)){
                    return false;
                }
            }
        }

        //重置排序后的数据
        item.reset(num);

        return true;
    }

    /**
     * 用于合并两个集合，对两个集合进行组合
     * @param lista
     * @param listb
     * @return
     */
    private ArrayList<Item> unionList(ArrayList<Item> lista, ArrayList<Item> listb) {
        int lena = lista.size();
        int lenb = listb.size();
        ArrayList<Item> retList = null;

        if(lena==0 && lenb==0){
            return lista;
        }else if(lena==0 && lenb>0){
            return listb;
        }else if(lena>=0 && lenb==0){
            return lista;
        }else{
            retList = new ArrayList<>(lena*lenb);
            //两个集合都包含元素，对符合条件的数据进行判断
            boolean useConfig = sharedPrefs.getBoolean("use_setting",false);
            boolean lessAfter = sharedPrefs.getBoolean("less_after",false);//前面的数比后面数小

            String matchType = sharedPrefs.getString("match_type","");
            eqType = getString(R.string.settings_item_option_equals).equals(matchType);
            //useConfig = false;
            Log.i(TAG, "unionList: less16=" + less16 + " eqType=" + eqType);

            int addCounter = 0;
            for(Item stra : lista){
                byte maxa = stra.maxVal();

                for(Item strb : listb){
                    if(lessAfter){
                        //在listb中查找是否有小于maxa的数
                        boolean pass = true;
                        for(byte b : strb.getData()){
                            if(b<maxa){
                                pass = false;
                                break;
                            }
                        }
                        if(!pass)   continue;//找下一组b，否则继续执行后面的操作
                    }

                    Item itemc = stra.add(strb);
                    if(!(useConfig && !NumUtil.checkOneOK(less16,eqType,itemc))){
                        retList.add(itemc);
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
     * @param list 返回的数据项
     * @param editText 输入的文本控件
     * @param spinner 下拉列表，选用多少个数据
     */
    private void getArrayList(ArrayList<Item> list,EditText editText, Spinner spinner) {
        //ArrayList<Integer> retList = new ArrayList<Integer>();
        String str = editText.getText().toString();
        int limit = spinner.getSelectedItemPosition();  //0,1,2
        if(spinner.getAdapter().getCount()==5){
            //如果是第一组5个元素，从1开始，下标需+1
            limit++;
        }
        Log.i(TAG, "getArrayList: str=" + str + " limit=" + limit);
        //ArrayList<String> resultList = NumUtil.getList(str,limit);
        for(String s : NumUtil.getList(str,limit)){
            list.add(new Item(s));
        }

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
