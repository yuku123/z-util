package com.zifang.util.zex.leetcode;

/**
 * LeetCode算法练习类。
 * <p>
 * 此类包含LeetCode题目的解决方案。
 * 练习算法和数据结构知识。
 *
 * @author zifang
 * @version 1.0
 */
import java.util.Arrays;
import java.util.HashSet;

public class _205 {
    public boolean isIsomorphic(String s, String t) {

        HashSet<Character> h1 = new HashSet<>();
        for(char c1 : s.toCharArray()){
            h1.add(c1);
        }

        HashSet<Character> h2 = new HashSet<>();
        for(char c2 : t.toCharArray()){
            h2.add(c2);
        }

        return h1.size() == h2.size();
    }

    public static void main(String[] args) {
        new _205().isIsomorphic("foo","bar");
    }
}
