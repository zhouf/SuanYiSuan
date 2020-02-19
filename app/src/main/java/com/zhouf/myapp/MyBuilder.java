package com.zhouf.myapp;

import java.util.ArrayList;
import java.util.List;

public class MyBuilder {

    private MyBuilder(){}

    public static Builder build(List<Byte> lista,List<Byte> listb,List<Byte> listc){
        return new Builder(lista,listb,listc);
    }

    static class Builder{
        private List<Pair> list;

        Builder(List<Byte> lista,List<Byte> listb,List<Byte> listc){
            list = new ArrayList<Pair>(1000);
            for(byte a : lista) {
                for (byte b : listb) {
                    for (byte c : listc) {
                        list.add(new Pair(a, b, c));
                    }
                }
            }
        }

        public List<Pair> getList(){
            return this.list;
        }

        public ArrayList<Item> listData(){
            //为列表准备数据
            ArrayList<Item> retList = new ArrayList<>(list.size());
            int index = 1;
            for(Pair p : list){
                Item item = new Item(index++,String.format("%d,%d,%d",p.a,p.b,p.c));
                retList.add(item);
            }
            return retList;
        }

        public Builder filterA(byte type){
            //type记录筛选一的位信息
            List<Pair> remove = new ArrayList<>(list.size());
            for(Pair p : list){
                if(p.isType0()){
                    //分组111
                    if((type&16)==0){
                        remove.add(p);
                    }
                }else if(p.isType1()){
                    //顺子
                    if((type&8)==0){
                        remove.add(p);
                    }
                }else if(p.isType2()){
                    //等差
                    if((type&4)==0){
                        remove.add(p);
                    }
                }else if(p.isType3()){
                    //对子
                    if((type&2)==0){
                        remove.add(p);
                    }
                }else{
                    //其它情况
                    if((type&1)==0){
                        remove.add(p);
                    }
                }
            }
            /*switch (type){
                case 0:
                    //分组，000，111，222，333...
                    for(Pair p : list){
                        if(!p.isType0()){
                            remove.add(p);
                        }
                    }
                    break;
                case 1:
                    //顺子
                    for(Pair p : list){
                        if(!p.isType1()){
                            remove.add(p);
                        }
                    }
                    break;
                case 2:
                    for(Pair p : list){
                        if(!p.isType2()){
                            remove.add(p);
                        }
                    }
                    break;
                case 3:
                    for(Pair p : list){
                        if(!p.isType3()){
                            remove.add(p);
                        }
                    }
                    break;
                case 4:
                    //组六，剩下的
                    for(Pair p : list){
                        if(p.isType0() || p.isType1() || p.isType2() || p.isType3()){
                            remove.add(p);
                        }
                    }
                    break;
            }*/
            list.removeAll(remove);
            return this;
        }

        public Builder filterB(byte scount){
            if(scount!=-1){
                List<Pair> remove = new ArrayList<>(list.size());
                //scount为单数个数
                for(Pair p : list){
                    if((p.a%2+p.b%2+p.c%2)!=scount){
                        remove.add(p);
                    }
                }
                list.removeAll(remove);
            }
            return this;
        }

        public Builder filterC(List<Byte> sumlist){
            //根据和进行过滤，三位数字相加和在list里返回true
            if(sumlist.size()>0){
                List<Pair> remove = new ArrayList<>(list.size());
                //scount为单数个数
                for(Pair p : list){
                    byte sum = (byte) (p.a+p.b+p.c);
                    if(!sumlist.contains(sum)){
                        remove.add(p);
                    }
                }
                list.removeAll(remove);
            }
            return this;
        }
    }
}
