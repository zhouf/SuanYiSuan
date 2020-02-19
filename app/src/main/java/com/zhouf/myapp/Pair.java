package com.zhouf.myapp;

public class Pair {
    byte a,b,c;
    Pair(byte a1,byte b1,byte c1){
        this.a = a1;
        this.b = b1;
        this.c = c1;
    }

    int getIntval(){
        return a*100+b*10+c;
    }
    boolean isType0(){
        return a==b && a==c;
    }
    boolean isType1(){
        //顺子
        byte max = a>b? (a>c? a : c) : (b>c? b : c);
        byte min = a<b? (a<c? a : c) : (b<c? b : c);
        byte mid = (byte) (a+b+c-max-min);
        return min+1==mid && mid+1==max;
    }
    boolean isType2(){
        //等差
        byte max = a>b? (a>c? a : c) : (b>c? b : c);
        byte min = a<b? (a<c? a : c) : (b<c? b : c);
        byte mid = (byte) (a+b+c-max-min);
        return (max-mid)==(mid-min);
    }
    boolean isType3(){
        //对子
        return a==b || a==c || b==c;
    }
}
