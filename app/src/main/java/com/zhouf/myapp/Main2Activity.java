package com.zhouf.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private int[] ckida = {R.id.numa0,R.id.numa1,R.id.numa2,R.id.numa3,R.id.numa4,R.id.numa5,R.id.numa6,R.id.numa7,R.id.numa8,R.id.numa9};
    private int[] ckidb = {R.id.numb0,R.id.numb1,R.id.numb2,R.id.numb3,R.id.numb4,R.id.numb5,R.id.numb6,R.id.numb7,R.id.numb8,R.id.numb9};
    private int[] ckidc = {R.id.numc0,R.id.numc1,R.id.numc2,R.id.numc3,R.id.numc4,R.id.numc5,R.id.numc6,R.id.numc7,R.id.numc8,R.id.numc9};
    private int[] filternumid = {R.id.ck_filter_num0,R.id.ck_filter_num1,R.id.ck_filter_num2,R.id.ck_filter_num3,R.id.ck_filter_num4,R.id.ck_filter_num5,R.id.ck_filter_num6,R.id.ck_filter_num7,R.id.ck_filter_num8,R.id.ck_filter_num9,R.id.ck_filter_num10,R.id.ck_filter_num11,R.id.ck_filter_num12,R.id.ck_filter_num13,R.id.ck_filter_num14,R.id.ck_filter_num15,R.id.ck_filter_num16,R.id.ck_filter_num17,R.id.ck_filter_num18,R.id.ck_filter_num19,R.id.ck_filter_num20,R.id.ck_filter_num21,R.id.ck_filter_num22,R.id.ck_filter_num23,R.id.ck_filter_num24,R.id.ck_filter_num25,R.id.ck_filter_num26,R.id.ck_filter_num27};
    private CheckBox[] ckBoxa;
    private CheckBox[] ckBoxb;
    private CheckBox[] ckBoxc;
    private CheckBox[] ckBoxAll;
    private CheckBox[] ckBoxNumAll;
    private Spinner spinner;
    private RadioGroup radioGroup;
    private RadioButton radio3d,radio3s,radio2d,radio2s;
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initCheckbox();

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: " + parent.getSelectedItemPosition());
                radio3d.setChecked(false);
                radio3s.setChecked(false);
                radio2d.setChecked(false);
                radio2s.setChecked(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioGroup = findViewById(R.id.rgroup);
        radio3d = findViewById(R.id.r3dan);
        radio3s = findViewById(R.id.r3shuang);
        radio2d = findViewById(R.id.r2dan);
        radio2s = findViewById(R.id.r2shuang);
    }

    private void initCheckbox(){
        ckBoxa = new CheckBox[10];
        ckBoxb = new CheckBox[10];
        ckBoxc = new CheckBox[10];
        ckBoxAll = new CheckBox[30];
        ckBoxNumAll = new CheckBox[28];
        for(int i=0;i<10;i++){
            CheckBox ck = findViewById(ckida[i]);
            ckBoxa[i] = ck;
            ckBoxAll[i] = ck;
        }
        for(int i=0;i<10;i++){
            CheckBox ck = findViewById(ckidb[i]);
            ckBoxb[i] = ck;
            ckBoxAll[i+10] = ck;
        }
        for(int i=0;i<10;i++){
            CheckBox ck = findViewById(ckidc[i]);
            ckBoxc[i] = ck;
            ckBoxAll[i+20] = ck;
        }

        for(int i=0;i<28;i++){
            CheckBox ck = findViewById(filternumid[i]);
            ckBoxNumAll[i] = ck;
        }

    }

    public void selectAll(View btn){
        for(CheckBox ck : ckBoxAll){
            ck.setChecked(true);
        }
    }
    public void resetAll(View btn){
        for(CheckBox ck : ckBoxAll){
            ck.setChecked(false);
        }
    }
    public void reservSel(View btn){
        for(CheckBox ck : ckBoxAll){
            ck.setChecked(!ck.isChecked());
        }
    }

    public void numSelectAll(View btn){
        for(CheckBox ck : ckBoxNumAll){
            ck.setChecked(true);
        }
    }
    public void numResetAll(View btn){
        for(CheckBox ck : ckBoxNumAll){
            ck.setChecked(false);
        }
    }
    public void numReservSel(View btn){
        for(CheckBox ck : ckBoxNumAll){
            ck.setChecked(!ck.isChecked());
        }
    }

    //全奇，全偶，全质，全合
    public void quanji(View btn){
        for(int i=1;i<=27;i+=2){
            ckBoxNumAll[i].setChecked(true);
        }
    }
    public void quanou(View btn){
        for(int i=0;i<27;i+=2){
            ckBoxNumAll[i].setChecked(true);
        }
    }
    public void quanzhi(View btn){
        for(int i=2;i<=27;i++){
            if(NumUtil.isPrime(i)){
                ckBoxNumAll[i].setChecked(true);
            }
        }
    }
    public void quanhe(View btn){
        for(int i=2;i<=27;i++){
            if(!NumUtil.isPrime(i)){
                ckBoxNumAll[i].setChecked(true);
            }
        }
    }

    public byte getRadioSelect(){
        //用数字返回单数个数，用radioGroup.getCheckedRadioButtonId()，会清不掉之前的选择
        byte ret = -1;
        if(radio3d.isChecked()){
            ret = 3;
        }else if(radio3s.isChecked()){
            ret = 0;
        }else if(radio2d.isChecked()){
            ret = 2;
        }else if(radio2s.isChecked()){
            ret = 1;
        }
        return ret;
    }

    public List<Byte> getSelect3(){
        //获取第三组过滤数据列表
        List<Byte> retList = new ArrayList<Byte>(30);
        for(byte i=0;i<ckBoxNumAll.length;i++){
            if(ckBoxNumAll[i].isChecked()){
                retList.add(i);
            }
        }
        return retList;
    }

    //查看结果
    public void show(View btn){
        MyBuilder.Builder builder = MyBuilder.build(getList(ckBoxa),getList(ckBoxb),getList(ckBoxc));

        List<Pair> list = builder.filterA(spinner.getSelectedItemPosition()).getList();
//        List<Pair> list = mergePair(getList(ckBoxa),getList(ckBoxb),getList(ckBoxc));
        Log.i(TAG, "show: list.size=" + list.size());
        for(Pair p : list){
            Log.i(TAG, "show: i=" + p.getIntval());
        }

        List<Pair> list2 = builder.filterB(getRadioSelect()).getList();
        Log.i(TAG, "show: list2.size=" + list2.size());
        for(Pair p : list2){
            Log.i(TAG, "show: i2=" + p.getIntval());
        }

        List<Pair> list3 = builder.filterC(getSelect3()).getList();
        Log.i(TAG, "show: list3.size=" + list3.size());
        for(Pair p : list3){
            Log.i(TAG, "show: i3=" + p.getIntval());
        }

        Log.i(TAG, "show: --------------------------------------------------------------");

        ArrayList<Item> listData = builder.listData();
        if(listData.size()==0) {
            Toast.makeText(this, R.string.no_data_tip, Toast.LENGTH_LONG).show();
        }else {
            Log.i(TAG, "onStartClick: 打开列表窗口");
            Intent listIntent = new Intent(this, MyListActivity.class);
            listIntent.putExtra("data", listData);
            startActivity(listIntent);
        }
    }

    @Deprecated
    private List<Pair> mergePair(List<Byte> lista,List<Byte> listb,List<Byte> listc){
        List<Pair> retList = new ArrayList<Pair>(1000);
        for(byte a : lista) {
            for (byte b : listb) {
                for (byte c : listc) {
                    retList.add(new Pair(a, b, c));
                }
            }
        }
        return retList;
    }

    private List<Pair> filterA(List<Pair> list){
        //进行筛选
        List<Pair> retList = new ArrayList<>(list.size());
        switch (spinner.getSelectedItemPosition()){
            case 0:
                //分组，000，111，222，333...
                for(Pair p : list){
                    if(p.isType0()){
                        retList.add(p);
                    }
                }
                break;
            case 1:
                //顺子
                for(Pair p : list){
                    if(p.isType1()){
                        retList.add(p);
                    }
                }
                break;
            case 2:
                for(Pair p : list){
                    if(p.isType2()){
                        retList.add(p);
                    }
                }
                break;
            case 3:
                for(Pair p : list){
                    if(p.isType3()){
                        retList.add(p);
                    }
                }
                break;
            case 4:
                break;
        }
        return retList;
    }

    private List<Byte> getList(CheckBox[] ckboxs){
        //将用户选择转换为列表
        List<Byte> retList = new ArrayList<>();
        for(byte i=0;i< ckboxs.length;i++){
            if(ckboxs[i].isChecked()){
                retList.add(i);
            }
        }
        return retList;
    }

//    class Pair{
//        byte a,b,c;
//        Pair(byte a1,byte b1,byte c1){
//            this.a = a1;
//            this.b = b1;
//            this.c = c1;
//        }
//
//        int getIntval(){
//            return a*100+b*10+c;
//        }
//        boolean isType0(){
//            return a==b && a==c;
//        }
//        boolean isType1(){
//            //顺子
//            byte max = a>b? (a>c? a : c) : (b>c? b : c);
//            byte min = a<b? (a<c? a : c) : (b<c? b : c);
//            byte mid = (byte) (a+b+c-max-min);
//            return min+1==mid && mid+1==max;
//        }
//        boolean isType2(){
//            //等差
//            byte max = a>b? (a>c? a : c) : (b>c? b : c);
//            byte min = a<b? (a<c? a : c) : (b<c? b : c);
//            byte mid = (byte) (a+b+c-max-min);
//            return (max-mid)==(mid-min);
//        }
//        boolean isType3(){
//            //对子
//            return a==b || a==c || b==c;
//        }
//    }
}
