package com.zhouf.myapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现排列组合的一个工具类
 */
public class NumUtil {

    private static final String TAG = "NUM";


    /**
     * 根据设定参数对数据进行过滤
     * @param listData 数据集合
     * @param dan 单数
     * @param zhi 质数
     * @param less16 1-16个数
     * @param eqType 是否精确匹配
     */
    public static void filterData(ArrayList<String> listData, int dan, int zhi, int less16, boolean eqType) {
        ArrayList<String> removed = new ArrayList<String>();
        for(String str : listData){
            int[] array = toIntArray(str);
            int d = countDan(array);
            int z = countZhi(array);
            int less = countLess16(array);
            //Log.i(TAG, "filterData:"+str+" d["+d+"] z["+z+"] less["+less+"]");
            if(eqType){
                //精确匹配
                if((dan>=0 && d!=dan) || (zhi>=0 && z!=zhi) || (less16>=0 && less!=less16)){
                    //加入待删除列表
                    removed.add(str);
                    //Log.i(TAG, "filterData: 移除");
                }
            }else{
                //不超过匹配
                if((dan>=0 && d>dan) || (zhi>=0 && z>zhi) || (less16>=0 && less>less16)){
                    //删除不符合条件的数据
                    removed.add(str);
                    //Log.i(TAG, "filterData: 移除");
                }
            }
        }

        listData.removeAll(removed);
    }

    /**
     * 统计单数元素个数
     * @param array
     * @return
     */
    private static int countDan(int[] array) {
        int ret = 0;
        for(int i : array){
            if(i%2==1){
                ret++;
            }
        }
        return ret;
    }

    /**
     * 统计质数
     * @param array
     * @return
     */
    private static int countZhi(int[] array){
        int ret = 0;
        for(int i : array){
            if(i>=2 && isPrime(i)){
                ret++;
            }
        }
        return ret;
    }
    /**
     * 统计1-16个数
     * @param array
     * @return
     */
    private static int countLess16(int[] array){
        int ret = 0;
        for(int i : array){
            if(i<=16){
                ret++;
            }
        }
        return ret;
    }

    private static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i < n; ++i)
            if (n % i == 0) return false;
        return true;
    }

    /**
     * 将字符串转换为整形数组
     * @param str 字串如"11,22,33,"
     * @return 整形数组
     */
    private static int[] toIntArray(String str) {
        String[] split = str.split(",");
        int[] ret = new int[split.length];
        for (int i = 0; i <split.length ; i++) {
            ret[i] = Integer.parseInt(split[i]);
        }
        return ret;
    }

    /**
     * 获取组合数
     * @param str 原始字符串
     * @param n 几位数，小于等于3
     * @return
     */
    public static ArrayList<String> getList(String str, int n) {
        ArrayList<String> retList = new ArrayList<String>();
        if(n==0||str.length()==0){
            return retList;
        }
        String a[] = str.replaceAll(" ",",").split(",");
        int len = a.length;

        Log.i(TAG, "getList: n=" + n);
        Log.i(TAG, "getList: len=" + len);
        //如果选取的数大于数组长度，返回数组所有内容
        if(n>=len){
            //直接将所有数据按一项写入列表
            String item = str.replaceAll(" ",",");
            if(!item.endsWith(",")){
                item = item.concat(",");
            }
            retList.add(item);
        }else{

            for(int i1 = 0 ; i1<=len-n ; i1++){
                if(n==1){
                    retList.add(a[i1].concat(","));
                }else{
                    for(int i2=i1+1;i2<=len-n+1;i2++){
                        if(n==2){
                            retList.add(a[i1] + "," + a[i2] + ",");
                        }else{
                            for(int i3=i2+1;i3<=len-n+2;i3++){
                                if(n==3){
                                    retList.add(a[i1] + "," + a[i2] + "," + a[i3] + ",");
                                }else{
                                    for(int i4=i3+1;i4<=len-n+3;i4++){
                                        if(n==4){
                                            retList.add(a[i1] + "," + a[i2] + "," + a[i3] + "," + a[i4] + ",");
                                        }else{
                                            for(int i5=i4+1;i5<len;i5++){
                                                retList.add(a[i1] + "," + a[i2] + "," + a[i3] + "," + a[i4] + "," + a[i5] + ",");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // end of for
        }
        return retList;
    }

    /**
     * 返回组合结果的字符串集合
     * @param str 原始字符串
     * @param n 选取个数
     * @return 字符串集合
     */
    @Deprecated
    public static ArrayList<String> getList1(String str, int n) {
        List<Integer> arr = new ArrayList<Integer>();
        for(String ss : str.split(",")){
            arr.add(Integer.parseInt(ss));
        }
        ArrayList<String> result = new ArrayList<String>();
        combinerSelect(arr, new ArrayList<Integer>(), arr.size(), n,result);
        return result;
    }

    /**
     * 实现组合算法
     * @param data 数据
     * @param workSpace 临时数据区
     * @param n 总体样本个数
     * @param k 选择个数
     * @param result 返回结果集合
     */
    private static void combinerSelect(List<Integer> data, List<Integer> workSpace, int n, int k,List<String> result) {
        List<Integer> copyData;
        List<Integer> copyWorkSpace;
        if (workSpace.size() == k) {
            StringBuilder sb = new StringBuilder();
            for (Object c : workSpace){
                sb.append(c).append(",");
            }
            result.add(sb.toString());
        }
        for (int i = 0; i < data.size(); i++) {
            copyData = new ArrayList<Integer>(data);
            copyWorkSpace = new ArrayList<Integer>(workSpace);
            copyWorkSpace.add(copyData.get(i));
            for (int j = i; j >= 0; j--)
                copyData.remove(j);
            combinerSelect(copyData, copyWorkSpace, n, k,result);
        }
    }

}
