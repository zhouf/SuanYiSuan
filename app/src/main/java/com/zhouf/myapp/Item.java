package com.zhouf.myapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    //每一项
    private int seq;
    private ArrayList<Byte> data;
    Item(){
        data = new ArrayList<Byte>(15);
    }

    /**
     * 初始化数据字串
     * @param str 如：1,3,6,12,23
     */
    Item(String str){
        this();
        if(str.length()>0){
            for(String s : str.split(",")){
                try{
                    data.add(Byte.valueOf(s.trim()));
                }catch(NumberFormatException|NullPointerException e){
                    // todo
                }
            }
        }

    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public ArrayList<Byte> getData() {
        return data;
    }

    public void reset(Byte array[]){
        data.removeAll(data);
        for(Byte b : array){
            data.add(b);
        }
    }

    public byte maxVal(){
        byte ret = Byte.MIN_VALUE;
        for(Byte b : data){
            if(b>ret){
                ret = b;
            }
        }
        return ret;
    }

    /**
     * 加入另一组数据的元素
     * @param i
     */
    public void append(Item i){
        data.addAll(i.data);
    }

    public Item add(Item i){
        Item ret = new Item();
        ret.data.addAll(data);
        ret.data.addAll(i.data);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Byte b : data){
            sb.append(b).append("-");
        }
        if(sb.toString().endsWith("-")){
            sb.deleteCharAt(sb.lastIndexOf("-"));
        }
        return sb.toString();
    }
}
