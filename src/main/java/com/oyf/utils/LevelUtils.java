package com.oyf.utils;

/**
 * Create Time: 2019年03月21日 17:05
 * Create Author: 欧阳飞
 **/

public class LevelUtils {

    //默认第一个层级
    public final static String ROOT = "0";

    public final static String SEPARATOR = ".";

    public static String calculateLevel(String parentLevel,Integer parentId){
        if (parentLevel == null){//没有父类层级，是第一个层级
            return ROOT;
        }
        return new StringBuffer().append(parentLevel).append(SEPARATOR).append(parentId).toString();
    }
}
