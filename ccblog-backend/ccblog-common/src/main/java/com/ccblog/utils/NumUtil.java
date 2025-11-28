package com.ccblog.utils;


import java.util.concurrent.ThreadLocalRandom;

/**
 * @author czc
 * @date 2025-09-23
 */
public class NumUtil {

    public static boolean upZero(Long num) {
        return num != null && num > 0;
    }

    public static Integer null2Zero(Integer num){
        return num==null?0:num;
    }

    public static Integer null2Zero(String num){
        return num==null||num.equals("null")?0:Integer.parseInt(num);
    }

    /**
     * 返回指定范围的随机数
     * @param min
     * @param max
     * @return
     */
    public static long randomLong(long min, long max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be < max");
        }
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    /**
     * 返回0-最大值的随机数
     * @param max
     * @return
     */
    public static long randomLong(long max) {
        return ThreadLocalRandom.current().nextLong(0, max);
    }

}
