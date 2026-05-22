package com.zifang.util.proxy.a.decompile.adapter;

import com.zifang.util.proxy.a.decompile.bean.Class_info;
import com.zifang.util.proxy.a.decompile.bean.ExceptionTable;
import com.zifang.util.proxy.a.decompile.bean.Fields_info;
import com.zifang.util.proxy.a.decompile.bean.Methods_info;
import com.zifang.util.proxy.a.decompile.bean.OpcodeAndOperand;
import com.zifang.util.proxy.a.decompile.bean.attribute.*;
import com.zifang.util.proxy.a.decompile.bean.constant.*;
import com.zifang.util.proxy.a.decompile.core.CodeConvertor;
import com.zifang.util.proxy.a.decompile.core.OperandBytesJudge;
import com.zifang.util.proxy.a.model.ClassFile;
import com.zifang.util.proxy.a.model.attribute.*;
import com.zifang.util.proxy.a.model.constantpool.*;
import com.zifang.util.proxy.a.model.readtype.U1;

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

    /** 临时存储 poolList，用于属性转换 */
    private static List<AbstractConstantPool> tempPoolList;

    /**
     * 将 ClassFile 转换为 Class_info
     *
     * @param classFile resolver2 的 ClassFile
     * @return decompile 的 Class_info
     */
    public static Class_info adapt(ClassFile classFile) {
        Class_info classInfo = new Class_info();
        tempPoolList = classFile.poolInfo.getPoolList();

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
                com.zifang.util.proxy.a.model.field.FieldInfo fieldInfo = 
                        classFile.fieldInfo.getField(i);
                Fields_info fieldsInfo = new Fields_info();
                fieldsInfo.setAccess_flag(String.format("%04X", fieldInfo.getAccessFlags().value));
                fieldsInfo.setName_index(fieldInfo.getNameIndex().value);
                fieldsInfo.setDescriptor_index(fieldInfo.getDescriptorIndex().value);
                fieldsInfo.setAttributes_count(fieldInfo.getAttributesCount().value);
                fieldsInfo.setAttributes_list(convertAttributes(fieldInfo.getAttributes()));
                fieldsList.add(fieldsInfo);
            }
        }
        classInfo.setFields_info_List(fieldsList);
        classInfo.setFields_count(fieldsList.size());

        // 8. 设置方法
        List<Methods_info> methodsList = new ArrayList<>();
        if (classFile.methodInfo != null) {
            for (int i = 0; i < classFile.methodInfo.getMethodCount(); i++) {
                com.zifang.util.proxy.a.model.method.MethodInfo methodInfo = 
                        classFile.methodInfo.getMethod(i);
                Methods_info methodsInfo = new Methods_info();
                methodsInfo.setAccess_flag(String.format("%04X", methodInfo.getAccessFlags().value));
                methodsInfo.setName_index(methodInfo.getNameIndex().value);
                methodsInfo.setDescriptor_index(methodInfo.getDescriptorIndex().value);
                methodsInfo.setAttributes_count(methodInfo.getAttributesCount().value);
                methodsInfo.setAttributes_list(convertAttributes(methodInfo.getAttributes()));
                methodsList.add(methodsInfo);
            }
        }
        classInfo.setMethods_info_List(methodsList);
        classInfo.setMethods_count(methodsList.size());

        // 9. 设置类属性（通常为空）
        classInfo.setAttributes_count(0);
        classInfo.setAttributes(new ArrayList<>());

        return classInfo;
    }

    /**
     * 转换属性列表
     */
    private static List<Attribute_info> convertAttributes(List<AbstractAttribute> attributes) {
        List<Attribute_info> result = new ArrayList<>();
        if (attributes == null) {
            return result;
        }

        for (AbstractAttribute attr : attributes) {
            Attribute_info info = convertAttribute(attr);
            if (info != null) {
                result.add(info);
            }
        }
        return result;
    }

    /**
     * 转换单个属性
     */
    private static Attribute_info convertAttribute(AbstractAttribute attr) {
        if (attr == null) {
            return null;
        }

        Attribute_info info = new Attribute_info();
        
        // 获取属性名称
        int nameIndex = attr.getAttributeNameIndex().value;
        String attrName = getUtf8String(nameIndex - 1);
        info.setAttribute_type(attrName);
        info.setAttribute_name_index(nameIndex);
        info.setAttrbute_length(attr.getAttributeLength().value);

        // 根据属性类型进行转换
        if (attr instanceof Code) {
            return convertCodeAttribute((Code) attr);
        } else if (attr instanceof ConstantValue) {
            return convertConstantValueAttribute((ConstantValue) attr);
        } else if (attr instanceof LineNumberTable) {
            return convertLineNumberTableAttribute((LineNumberTable) attr);
        } else if (attr instanceof LocalVariableTable) {
            return convertLocalVariableTableAttribute((LocalVariableTable) attr);
        }

        // 其他属性类型直接存储信息
        info.setInfo(attr.getClass().getSimpleName());
        return info;
    }

    /**
     * 转换 Code 属性
     */
    private static Attribute_Code_info convertCodeAttribute(Code code) {
        Attribute_Code_info info = new Attribute_Code_info();
        info.setAttribute_type("Code");
        info.setAttribute_name_index(code.getAttributeNameIndex().value);
        info.setAttrbute_length(code.getAttributeLength().value);
        info.setMax_statck(code.getMaxStack().value);
        info.setMax_locals(code.getMaxLocals().value);
        info.setCode_length(code.getCodeLength().value);

        // 转换字节码指令
        Map<Integer, OpcodeAndOperand> codeMap = new HashMap<>();
        List<U1> codeBytes = code.getCode();
        for (int pc = 0; pc < codeBytes.size(); pc++) {
            int opcode = codeBytes.get(pc).value & 0xFF;
            OpcodeAndOperand oa = new OpcodeAndOperand();
            String hexCode = String.format("%02X", opcode);
            String opcodeName = CodeConvertor.codeConvertor(hexCode);
            
            // 处理未知 opcode
            if (opcodeName == null) {
                opcodeName = "unknown_" + hexCode;
                codeMap.put(pc, oa);
                continue;
            }
            
            oa.setOpcode(opcodeName);
            
            // 判断操作数
            int operandBytes = OperandBytesJudge.operandBytesCount(opcodeName);
            if (operandBytes > 0 && pc + operandBytes < codeBytes.size()) {
                StringBuilder operand = new StringBuilder();
                for (int j = 0; j < operandBytes; j++) {
                    pc++;
                    operand.append(String.format("%02X", codeBytes.get(pc).value));
                }
                try {
                    oa.setOperand(Integer.parseInt(operand.toString(), 16));
                } catch (NumberFormatException e) {
                    oa.setOperand(operand.toString());
                }
            }
            codeMap.put(pc, oa);
        }
        info.setCodeMap(codeMap);

        // 转换异常表
        info.setException_table_length(code.getExceptionTableLength().value);
        List<ExceptionTable> exceptionTables = new ArrayList<>();
        for (Code.ExceptionInfo ei : code.getExceptionTable()) {
            ExceptionTable et = new ExceptionTable();
            et.setStart_pc(ei.getStartPc().value);
            et.setEnd_pc(ei.getEndPc().value);
            et.setHandler_pc(ei.getHandlerPc().value + "");
            // catch_pc 0 表示 catch-all (finally)
            if (ei.getCatchPc().value == 0) {
                et.setCatch_type("0");
            } else {
                et.setCatch_type(ei.getCatchPc().value + "");
            }
            exceptionTables.add(et);
        }
        info.setExceptionTablesList(exceptionTables);

        // 转换子属性（LineNumberTable, LocalVariableTable）
        info.setAttributes_count(code.getAttributesCount().value);
        info.setAttributesList(convertAttributes(code.getAttributes()));

        return info;
    }

    /**
     * 转换 ConstantValue 属性
     */
    private static Attribute_ConstantValue_info convertConstantValueAttribute(ConstantValue cv) {
        Attribute_ConstantValue_info info = new Attribute_ConstantValue_info();
        info.setAttribute_type("ConstantValue");
        info.setAttribute_name_index(cv.getAttributeNameIndex().value);
        info.setAttrbute_length(cv.getAttributeLength().value);
        info.setConstantvalue_index(cv.getConstantValueIndex().value);
        return info;
    }

    /**
     * 转换 LineNumberTable 属性
     */
    private static Attribute_LineNumberTable_info convertLineNumberTableAttribute(LineNumberTable lnt) {
        Attribute_LineNumberTable_info info = new Attribute_LineNumberTable_info();
        info.setAttribute_type("LineNumberTable");
        info.setAttribute_name_index(lnt.getAttributeNameIndex().value);
        info.setAttrbute_length(lnt.getAttributeLength().value);
        info.setLine_number_table_length(lnt.getLineNumTableLength().value);

        List<Line_number_info> lineList = new ArrayList<>();
        for (LineNumberTable.LineNumberInfo lni : lnt.getLineNumberTable()) {
            Line_number_info linfo = new Line_number_info();
            linfo.setStart_pc(lni.getStartPc().value);
            linfo.setLine_number(lni.getLineNumber().value);
            lineList.add(linfo);
        }
        info.setLine_number_table_List(lineList);

        return info;
    }

    /**
     * 转换 LocalVariableTable 属性
     */
    private static Attribute_LocalVariableTable_info convertLocalVariableTableAttribute(LocalVariableTable lvt) {
        Attribute_LocalVariableTable_info info = new Attribute_LocalVariableTable_info();
        info.setAttribute_type("LocalVariableTable");
        info.setAttribute_name_index(lvt.getAttributeNameIndex().value);
        info.setAttrbute_length(lvt.getAttributeLength().value);
        info.setLocal_variable_table_length(lvt.getLocalVariableTableLength().value);

        List<Local_variable_info> varList = new ArrayList<>();
        for (LocalVariableTable.LocalVariableInfo lvi : lvt.getLocalVariableTable()) {
            Local_variable_info vinfo = new Local_variable_info();
            vinfo.setStart_pc(lvi.getStartPc().value);
            vinfo.setLength(lvi.getLength().value);
            vinfo.setName_index(lvi.getNameIndex().value);
            vinfo.setDescriptor_index(lvi.getDescriptorIndex().value);
            vinfo.setIndex(lvi.getIndex().value);
            varList.add(vinfo);
        }
        info.setLocal_variable_table_List(varList);

        return info;
    }

    /**
     * 从常量池获取 UTF8 字符串
     */
    private static String getUtf8String(int poolIndex) {
        if (poolIndex < 0 || poolIndex >= tempPoolList.size()) {
            return "";
        }
        AbstractConstantPool pool = tempPoolList.get(poolIndex);
        if (pool instanceof Utf8Info) {
            return ((Utf8Info) pool).getValue();
        }
        return "";
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