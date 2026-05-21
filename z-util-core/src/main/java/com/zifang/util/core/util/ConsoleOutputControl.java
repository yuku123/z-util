package com.zifang.util.core.util;

import java.util.HashMap;

/**
 * 控制台彩色输出工具类。
 * <p>
 * 提供在终端输出彩色文字和背景的方法，基于 ANSI 转义序列实现。
 * <p>
 * 支持的颜色包括：红色、绿色、黄色、蓝色、紫色、青色、灰色等。
 *
 * @author zifang
 * @see <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI Escape Code</a>
 */
public class ConsoleOutputControl {

    private static final HashMap<Integer,String> colorMap = new HashMap<Integer,String>(){
        {
            put(31,"红色字体");
            put(32,"绿色字体");
            put(33,"黄色字体");
            put(34,"蓝色字体");
            put(35,"紫色字体");
            put(36,"青色字体");
            put(37,"灰色字体");
            put(40,"黑色背景");
            put(41,"红色背景");
            put(42,"绿色背景");
            put(43,"黄色背景");
            put(44,"蓝色背景");
            put(45,"紫色背景");
            put(46,"青色背景");
            put(47,"灰色背景");
        }
    };

    public static String getColoredString(int color,int fontType,String content){
        return String.format("\033[%d;%dm%s\033[0m",color,fontType,content);
    }

    public static void main(String[] args) {
        for(int i=0;i<7;i++){
            System.out.println(getColoredString(31+i,4,"颜色控制 -> "+colorMap.get(31+i)));
        }
        for(int i=0;i<8;i++){
            System.out.println(getColoredString(40+i,3,"背景控制 -> "+colorMap.get(40+i)));
        }
        System.out.println(String.format("\033[%d;%d;%dm%s\033[0m",6,11,11,"文 字 背 景 "));

        System.out.println(String.format("\033[%d;%d;%dm%s\033[0m",6,11,11,"文 字 背 景 "));
    }
}
