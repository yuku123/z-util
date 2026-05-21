package com.zifang.util.core.lang.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态类单元，包含主类和子类列表。
 * <p>
 * 用于组织具有层级关系的动态类结构。
 *
 * @author zifang
 * @see DynamicClass
 */
public class DynamicClassUnit {

    private DynamicClass main;

    private List<DynamicClassUnit> sub = new ArrayList<>();

    public DynamicClass getMain() {
        return main;
    }

    public void setMain(DynamicClass main) {
        this.main = main;
    }

    public List<DynamicClassUnit> getSub() {
        return sub;
    }

    public void setSub(List<DynamicClassUnit> sub) {
        this.sub = sub;
    }

    public List<DynamicClass> collect() {
        List<DynamicClass> dynamicClasses = new ArrayList<>();
        dynamicClasses.add(main);
        for(DynamicClassUnit dynamicClassUnit : sub){
            dynamicClasses.addAll(dynamicClassUnit.collect());
        }
        return dynamicClasses;
    }
}
