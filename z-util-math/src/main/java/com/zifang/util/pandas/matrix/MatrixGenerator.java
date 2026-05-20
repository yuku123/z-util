package com.zifang.util.pandas.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MatrixGenerator 类 - 矩阵构造器
 * 提供多种方式创建矩阵，类似于 numpy 矩阵生成函数
 */
public class MatrixGenerator {


    /**
     * 从 List 创建矩阵
     * @param list 包含矩阵元素的列表
     * @return Matrix 实例
     */
    public Matrix array(List<? extends Object> list) {
        Matrix numpyArray = new Matrix();
        return numpyArray;
    }

    /**
     * 从 double 数组创建矩阵
     * @param arrys double 类型数组
     * @return Matrix 实例
     */
    public Matrix array(double[] arrys) {
        List<? extends Object> list = Arrays.asList(arrys);
        Matrix numpyArray = new Matrix();
        return numpyArray;
    }

//    public  NumpyArray array(E[] arrys,String type){
//        List<? extends Object> list =  Arrays.asList(arrys);
//        NumpyArray numpyArray = new NumpyArray();
//        numpyArray.setArray(list);
//        return numpyArray;
//    }


    /**
     * 生成指定维度的全零矩阵，类似于 numpy.zeros()
     * <p>
     * 示例：dimension=3 返回
     * <pre>
     * [ 0 0 0
     *  0 0 0
     *  0 0 0 ]
     * </pre>
     * @param dimension 矩阵维度（方阵）
     * @return 全零矩阵
     */
    public static Matrix zeros(Integer dimension) {
        return null;
    }

    /**
     * 创建一个空矩阵
     */
    public void empty() {
    }

    /**
     * 生成指定范围的数组，类似于 numpy.arange()
     * @return 生成的数组
     */
    public void arrange() {
    }

    /**
     * 生成指定范围的数组
     * @param start 起始值（包含）
     * @param end 结束值（不包含）
     * @return 生成的数组
     */
    public void arrange(Integer start, Integer end) {
    }

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("a");
        arrayList.add("n");
        MatrixGenerator numpy = new MatrixGenerator();
        numpy.array(arrayList);
    }

}
