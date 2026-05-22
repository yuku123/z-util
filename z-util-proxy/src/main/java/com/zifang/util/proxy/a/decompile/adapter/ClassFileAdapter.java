package com.zifang.util.proxy.a.decompile.adapter;

import com.zifang.util.proxy.a.decompile.bean.Class_info;
import com.zifang.util.proxy.a.decompile.bean.Fields_info;
import com.zifang.util.proxy.a.decompile.bean.Methods_info;
import com.zifang.util.proxy.a.decompile.bean.constant.*;
import com.zifang.util.proxy.a.resolver2.ClassFile;
import com.zifang.util.proxy.a.resolver2.constantpool.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassFile 适配器
 * <p>
 * 将 resolver2.ClassFile 转换为 decompile.bean.Class_info，
 * 使现有的 SrcCreator 无需修改即可使用。
 */
public class ClassFileAdapter {

    /**
     * 将 ClassFile 转换为 Class_info
     *
     * @param classFile resolver2 的 ClassFile
     * @return decompile 的 Class_info
     */
    public static Class_info adapt(ClassFile classFile) {
        Class_info classInfo = new Class_info();

        // 1. 设置魔数
        classInfo.setMagic(String.format("0x%08X", classFile.magic.value));

        // 2. 设置版本号
        classInfo.setMinor_version(classFile.minorVersion.value + "");
        classInfo.setMajor_version(classFile.majorVersion.value + "");

        // 3. 设置常量池
        Map<Integer, Constant_X_info> poolMap = new HashMap<>();
        List<AbstractConstantPool> poolList = classFile.poolInfo.getPoolList();
        for (int i = 0; i < poolList.size(); i++) {
            AbstractConstantPool pool = poolList.get(i);
            poolMap.put(i, convertConstantPool(pool));
        }
        classInfo.setConstant_pool_Map(poolMap);
        classInfo.setCp_count(poolList.size() + 1);

        // 4. 设置访问标志
        classInfo.setAccess_flag(String.format("%04X", classFile.accessFlag.value));

        // 5. 设置 this class 和 super class
        classInfo.setThis_class_index(classFile.classIndex.value);
        classInfo.setSuper_class_index(classFile.superClassIndex.value);

        // 6. 设置接口
        classInfo.setInterfaces_count(classFile.interfaceIndex.list.size());
        classInfo.setInterfacesList(new ArrayList<>());
        for (int i = 0; i < classFile.interfaceIndex.list.size(); i++) {
            classInfo.getInterfacesList().add((int) classFile.interfaceIndex.list.get(i).index.value);
        }

        // 7. 设置字段
        List<Fields_info> fieldsList = new ArrayList<>();
        if (classFile.fieldInfo != null) {
            for (int i = 0; i < classFile.fieldInfo.getFieldCount(); i++) {
                com.zifang.util.proxy.a.resolver2.field.FieldInfo fieldInfo = 
                        classFile.fieldInfo.getField(i);
                Fields_info fieldsInfo = new Fields_info();
                fieldsInfo.setAccess_flag(String.format("%04X", fieldInfo.getAccessFlags().value));
                fieldsInfo.setName_index(fieldInfo.getNameIndex().value);
                fieldsInfo.setDescriptor_index(fieldInfo.getDescriptorIndex().value);
                fieldsInfo.setAttributes_count(fieldInfo.getAttributesCount().value);
                // TODO: 转换属性
                fieldsInfo.setAttributes_list(new ArrayList<>());
                fieldsList.add(fieldsInfo);
            }
        }
        classInfo.setFields_info_List(fieldsList);
        classInfo.setFields_count(fieldsList.size());

        // 8. 设置方法
        List<Methods_info> methodsList = new ArrayList<>();
        if (classFile.methodInfo != null) {
            for (int i = 0; i < classFile.methodInfo.getMethodCount(); i++) {
                com.zifang.util.proxy.a.resolver2.method.MethodInfo methodInfo = 
                        classFile.methodInfo.getMethod(i);
                Methods_info methodsInfo = new Methods_info();
                methodsInfo.setAccess_flag(String.format("%04X", methodInfo.getAccessFlags().value));
                methodsInfo.setName_index(methodInfo.getNameIndex().value);
                methodsInfo.setDescriptor_index(methodInfo.getDescriptorIndex().value);
                methodsInfo.setAttributes_count(methodInfo.getAttributesCount().value);
                // TODO: 转换属性
                methodsInfo.setAttributes_list(new ArrayList<>());
                methodsList.add(methodsInfo);
            }
        }
        classInfo.setMethods_info_List(methodsList);
        classInfo.setMethods_count(methodsList.size());

        return classInfo;
    }

    /**
     * 转换常量池项
     */
    private static Constant_X_info convertConstantPool(AbstractConstantPool pool) {
        if (pool instanceof Utf8Info) {
            Utf8Info utf8 = (Utf8Info) pool;
            Constant_Utf8_info info = new Constant_Utf8_info();
            info.setTag("1");
            info.setLength(utf8.getValue().length());
            info.setBytes(utf8.getValue());
            info.setConstant_pool_info_Type("Constant_Utf8_info");
            return info;
        } else if (pool instanceof ClassInfo) {
            // ClassInfo 对应 CONSTANT_Class_info (tag=7)
            ClassInfo classInfo = (ClassInfo) pool;
            Constant_Class_info info = new Constant_Class_info();
            info.setTag("7");
            info.setIndex(classInfo.getNameIndex().value);
            info.setConstant_pool_info_Type("Constant_Class_info");
            return info;
        } else if (pool instanceof ConstantClassInfo) {
            // ConstantClassInfo 对应 CONSTANT_String_info (tag=8)
            ConstantClassInfo stringInfo = (ConstantClassInfo) pool;
            Constant_String_info info = new Constant_String_info();
            info.setTag("8");
            info.setIndex(stringInfo.getStringIndex().value);
            info.setConstant_pool_info_Type("Constant_String_info");
            return info;
        } else if (pool instanceof FieldRefInfo) {
            // FieldRefInfo: classIndex -> index, nameIndex -> index2
            FieldRefInfo ref = (FieldRefInfo) pool;
            Constant_Fieldref_info info = new Constant_Fieldref_info();
            info.setTag("9");
            info.setIndex(ref.getClassIndex().value);
            info.setIndex2(ref.getNameIndex().value);
            info.setConstant_pool_info_Type("Constant_Fieldref_info");
            return info;
        } else if (pool instanceof MethodRefInfo) {
            // MethodRefInfo: classIndex -> index, nameIndex -> index2
            MethodRefInfo ref = (MethodRefInfo) pool;
            Constant_Methodref_info info = new Constant_Methodref_info();
            info.setTag("10");
            info.setIndex(ref.getClassIndex().value);
            info.setIndex2(ref.getNameIndex().value);
            info.setConstant_pool_info_Type("Constant_Methodref_info");
            return info;
        } else if (pool instanceof ConstantInterfaceMethodRefInfo) {
            ConstantInterfaceMethodRefInfo ref = (ConstantInterfaceMethodRefInfo) pool;
            Constant_InterfaceMethodref_info info = new Constant_InterfaceMethodref_info();
            info.setTag("11");
            info.setIndex(ref.getClassIndex().value);
            info.setIndex2(ref.getNameIndex().value);
            info.setConstant_pool_info_Type("Constant_InterfaceMethodref_info");
            return info;
        } else if (pool instanceof ConstantNameAndTypeInfo) {
            ConstantNameAndTypeInfo nat = (ConstantNameAndTypeInfo) pool;
            Constant_NameAndType_info info = new Constant_NameAndType_info();
            info.setTag("12");
            info.setIndex(nat.getNameIndex().value);
            info.setIndex2(nat.getDescriptorIndex().value);
            info.setConstant_pool_info_Type("Constant_NameAndType_info");
            return info;
        } else if (pool instanceof ConstantIntegerInfo) {
            ConstantIntegerInfo integer = (ConstantIntegerInfo) pool;
            Constant_Integer_info info = new Constant_Integer_info();
            info.setTag("3");
            info.setBytes(integer.getBytes().value + "");
            info.setConstant_pool_info_Type("Constant_Integer_info");
            return info;
        } else if (pool instanceof ConstantFloatInfo) {
            ConstantFloatInfo floatInfo = (ConstantFloatInfo) pool;
            Constant_Float_info info = new Constant_Float_info();
            info.setTag("4");
            info.setBytes(floatInfo.getBytes().value + "");
            info.setConstant_pool_info_Type("Constant_Float_info");
            return info;
        } else if (pool instanceof ConstantLongInfo) {
            ConstantLongInfo longInfo = (ConstantLongInfo) pool;
            Constant_Long_info info = new Constant_Long_info();
            info.setTag("5");
            info.setBytes(longInfo.getBytes().getValue() + "");
            info.setConstant_pool_info_Type("Constant_Long_info");
            return info;
        } else if (pool instanceof ConstantDoubleInfo) {
            ConstantDoubleInfo doubleInfo = (ConstantDoubleInfo) pool;
            Constant_Double_info info = new Constant_Double_info();
            info.setTag("6");
            info.setBytes(doubleInfo.getBytes().getValue() + "");
            info.setConstant_pool_info_Type("Constant_Double_info");
            return info;
        } else if (pool instanceof ConstantMethodHandleInfo) {
            ConstantMethodHandleInfo mh = (ConstantMethodHandleInfo) pool;
            Constant_MethodHandle_info info = new Constant_MethodHandle_info();
            info.setTag("15");
            info.setReference_kind(mh.getReferenceKind().value);
            info.setReference_index(mh.getReferenceIndex().value);
            info.setConstant_pool_info_Type("Constant_MethodHandle_info");
            return info;
        } else if (pool instanceof ConstantMethodTypeInfo) {
            ConstantMethodTypeInfo mt = (ConstantMethodTypeInfo) pool;
            Constant_MethodType_info info = new Constant_MethodType_info();
            info.setTag("16");
            info.setDescriptor_index(mt.getDescriptorIndex().value);
            info.setConstant_pool_info_Type("Constant_MethodType_info");
            return info;
        } else if (pool instanceof ConstantInvokeDynamicInfo) {
            ConstantInvokeDynamicInfo id = (ConstantInvokeDynamicInfo) pool;
            Constant_InvokeDynamic_info info = new Constant_InvokeDynamic_info();
            info.setTag("18");
            info.setBootstrap_method_attr_index(id.getBootstrapMethodAttrIndex().value);
            info.setName_and_type_index(id.getNameIndex().value);
            info.setConstant_pool_info_Type("Constant_InvokeDynamic_info");
            return info;
        }

        // 默认返回空实现
        Constant_X_info info = new Constant_X_info();
        info.setConstant_pool_info_Type(pool.getClass().getSimpleName());
        return info;
    }
}