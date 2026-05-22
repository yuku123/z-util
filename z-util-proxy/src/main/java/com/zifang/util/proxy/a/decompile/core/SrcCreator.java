package com.zifang.util.proxy.a.decompile.core;

import com.zifang.util.proxy.a.model.ClassFile;
import com.zifang.util.proxy.a.model.attribute.AbstractAttribute;
import com.zifang.util.proxy.a.model.attribute.Code;
import com.zifang.util.proxy.a.model.attribute.LineNumberTable;
import com.zifang.util.proxy.a.model.attribute.LocalVariableTable;
import com.zifang.util.proxy.a.model.constantpool.*;
import com.zifang.util.proxy.a.model.field.FieldInfo;
import com.zifang.util.proxy.a.model.method.MethodInfo;
import com.zifang.util.proxy.a.model.readtype.U1;
import com.zifang.util.proxy.a.model.readtype.U2;

import java.util.*;
import java.util.Map.Entry;

/**
 * 源码生成器
 * <p>
 * 将 ClassFile 反编译为 Java 源代码。
 */
public class SrcCreator {

    /**
     * 根据 ClassFile 对象，生成 Java 源代码
     *
     * @param classFile ClassFile 对象
     * @return Java 源代码字符串
     */
    public static String createJavaFileSrc(ClassFile classFile) {
        StringBuffer sBuffer = new StringBuffer();

        // 获取常量池
        List<AbstractConstantPool> poolList = classFile.poolInfo.getPoolList();

        // 获取类名
        String thisClassQualified = classFile.getClassName();
        if (thisClassQualified == null) {
            thisClassQualified = "UnknownClass";
        }
        String thisClassSimple = thisClassQualified.substring(thisClassQualified.lastIndexOf("/") + 1);

        // 包声明
        String packageName = thisClassQualified.substring(0, thisClassQualified.lastIndexOf("/")).replace("/", ".");
        sBuffer.append("package ").append(packageName).append(";\n\n");

        // 收集导入
        Set<String> imports = collectImports(classFile);

        // 输出导入
        for (String imp : imports) {
            // 过滤掉 java.lang、当前包、自己、不合法的导入
            if (!imp.startsWith("java.lang.") 
                    && !imp.equals(packageName)
                    && !imp.equals(packageName + "." + thisClassSimple)) {
                sBuffer.append("import ").append(imp).append(";\n");
            }
        }
        sBuffer.append("\n");

        // 类访问标志
        String accessFlag = AccessFlagConvertor.classAccessFlagConvertor(
                String.format("%04X", classFile.accessFlag.value));

        // 父类
        String superClass = getSuperClassName(classFile);

        // 接口
        String interfaces = getInterfaceNames(classFile);

        sBuffer.append(accessFlag).append("class ").append(thisClassSimple)
                .append(superClass).append(interfaces).append("{\n\n");

        // 字段
        if (classFile.fieldInfo != null) {
            for (int i = 0; i < classFile.fieldInfo.getFieldCount(); i++) {
                FieldInfo fieldInfo = classFile.fieldInfo.getField(i);
                sBuffer.append(generateFieldSrc(fieldInfo, classFile));
            }
        }
        sBuffer.append("\n");

        // 方法
        if (classFile.methodInfo != null) {
            for (int i = 0; i < classFile.methodInfo.getMethodCount(); i++) {
                MethodInfo methodInfo = classFile.methodInfo.getMethod(i);
                String methodSrc = generateMethodSrc(methodInfo, classFile, thisClassSimple);
                if (methodSrc != null) {
                    sBuffer.append(methodSrc);
                }
            }
        }

        sBuffer.append("}\n");
        return sBuffer.toString();
    }

    /**
     * 收集需要导入的类
     */
    private static Set<String> collectImports(ClassFile classFile) {
        Set<String> imports = new HashSet<>();
        List<AbstractConstantPool> poolList = classFile.poolInfo.getPoolList();

        for (AbstractConstantPool pool : poolList) {
            if (pool instanceof Utf8Info) {
                String value = ((Utf8Info) pool).getValue();
                if (value != null && value.contains("/") && !value.contains("(")) {
                    String imported = value.replace("/", ".");
                    if (imported.contains("<")) {
                        imported = imported.substring(0, imported.indexOf("<"));
                    }
                    if (!imported.endsWith(";")) {
                        imports.add(imported);
                    }
                }
            }
        }
        return imports;
    }

    /**
     * 获取父类名
     */
    private static String getSuperClassName(ClassFile classFile) {
        if (classFile.superClassIndex == null || classFile.superClassIndex.value == 0) {
            return " extends Object";
        }

        List<AbstractConstantPool> poolList = classFile.poolInfo.getPoolList();
        int classInfoIndex = classFile.superClassIndex.value - 1;

        if (classInfoIndex < 0 || classInfoIndex >= poolList.size()) {
            return " extends Object";
        }

        AbstractConstantPool pool = poolList.get(classInfoIndex);
        if (!(pool instanceof ClassInfo)) {
            return " extends Object";
        }

        int utf8Index = ((ClassInfo) pool).getNameIndex().value - 1;
        if (utf8Index < 0 || utf8Index >= poolList.size()) {
            return " extends Object";
        }

        AbstractConstantPool utf8Pool = poolList.get(utf8Index);
        if (!(utf8Pool instanceof Utf8Info)) {
            return " extends Object";
        }

        String superClass = ((Utf8Info) utf8Pool).getValue();
        if (superClass.equals("java/lang/Object")) {
            return "";
        }
        return " extends " + superClass.replace("/", ".");
    }

    /**
     * 获取接口名列表
     */
    private static String getInterfaceNames(ClassFile classFile) {
        if (classFile.interfaceIndex == null || classFile.interfaceIndex.list.isEmpty()) {
            return "";
        }

        List<AbstractConstantPool> poolList = classFile.poolInfo.getPoolList();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < classFile.interfaceIndex.list.size(); i++) {
            int classInfoIndex = (int) classFile.interfaceIndex.list.get(i).index.value - 1;
            if (classInfoIndex < 0 || classInfoIndex >= poolList.size()) {
                continue;
            }

            AbstractConstantPool pool = poolList.get(classInfoIndex);
            if (!(pool instanceof ClassInfo)) {
                continue;
            }

            int utf8Index = ((ClassInfo) pool).getNameIndex().value - 1;
            if (utf8Index < 0 || utf8Index >= poolList.size()) {
                continue;
            }

            AbstractConstantPool utf8Pool = poolList.get(utf8Index);
            if (!(utf8Pool instanceof Utf8Info)) {
                continue;
            }

            String iface = ((Utf8Info) utf8Pool).getValue().replace("/", ".");
            if (i == 0) {
                sb.append(" implements ");
            } else {
                sb.append(", ");
            }
            sb.append(iface);
        }
        return sb.toString();
    }

    /**
     * 生成字段源代码
     */
    private static String generateFieldSrc(FieldInfo fieldInfo, ClassFile classFile) {
        List<AbstractConstantPool> poolList = classFile.poolInfo.getPoolList();

        // 访问标志
        String accessFlag = AccessFlagConvertor.fieldAccessFlagConvertor(
                String.format("%04X", fieldInfo.getAccessFlags().value));

        // 字段名
        int nameIndex = fieldInfo.getNameIndex().value - 1;
        String fieldName = getUtf8String(poolList, nameIndex);

        // 描述符
        int descIndex = fieldInfo.getDescriptorIndex().value - 1;
        String descriptor = getUtf8String(poolList, descIndex);

        // 字段类型
        String fieldType = ParamsConvertor.paramsConvertorFieldType(descriptor);

        return "\t" + accessFlag + fieldType + " " + fieldName + ";\n";
    }

    /**
     * 生成方法源代码
     */
    private static String generateMethodSrc(MethodInfo methodInfo, ClassFile classFile, String thisClassSimple) {
        List<AbstractConstantPool> poolList = classFile.poolInfo.getPoolList();

        // 方法名
        int nameIndex = methodInfo.getNameIndex().value - 1;
        String methodName = getUtf8String(poolList, nameIndex);

        // 跳过类初始化方法
        if ("<clinit>".equals(methodName)) {
            return null;
        }

        // 描述符
        int descIndex = methodInfo.getDescriptorIndex().value - 1;
        String descriptor = getUtf8String(poolList, descIndex);

        // 访问标志
        String accessFlag = AccessFlagConvertor.methodAccessFlagConvertor(
                String.format("%04X", methodInfo.getAccessFlags().value));

        // 构造方法处理
        if ("<init>".equals(methodName)) {
            methodName = thisClassSimple;
        }

        // 方法签名
        String methodSignature = jointMethodReturnNameParams(methodName, descriptor);

        // 异常
        String exceptions = throwExceptionsJudge(methodInfo, classFile);

        // 判断是否是抽象或 native 方法
        short accessValue = methodInfo.getAccessFlags().value;
        boolean isAbstract = (accessValue & 0x0400) != 0;
        boolean isNative = (accessValue & 0x0100) != 0;

        if (isAbstract || isNative) {
            return "\t" + accessFlag + methodSignature + exceptions + ";\n\n";
        }

        // 获取 Code 属性
        Code codeAttr = findCodeAttribute(methodInfo);

        StringBuffer methodBody = new StringBuffer();
        methodBody.append("\t").append(accessFlag).append(methodSignature)
                .append(exceptions).append("{\n");

        if (codeAttr != null) {
            // 生成方法体
            String body = generateMethodBody(codeAttr, classFile);
            methodBody.append(body);
        }

        methodBody.append("\t}\n\n");
        return methodBody.toString();
    }

    /**
     * 查找方法的 Code 属性
     */
    private static Code findCodeAttribute(MethodInfo methodInfo) {
        if (methodInfo.getAttributes() == null) {
            return null;
        }
        for (AbstractAttribute attr : methodInfo.getAttributes()) {
            if (attr instanceof Code) {
                return (Code) attr;
            }
        }
        return null;
    }

    /**
     * 生成方法体
     */
    private static String generateMethodBody(Code codeAttr, ClassFile classFile) {
        StringBuffer sb = new StringBuffer();

        // 收集局部变量表
        Map<Integer, FieldNameAndType> variableNameMap = new HashMap<>();
        collectLocalVariables(codeAttr, classFile, variableNameMap);

        // 收集行号表
        TreeMap<Integer, Integer> lineMap = new TreeMap<>();
        collectLineNumbers(codeAttr, classFile, lineMap);

        // 获取字节码
        List<U1> codeBytes = codeAttr.getCode();
        Map<Integer, OpcodeAndOperand> codeMap = decodeBytecode(codeBytes);

        // 简化处理：直接输出所有 opcode 的字符串表示
        if (lineMap.isEmpty()) {
            // 单行方法体
            sb.append("\t\t// bytecode: ");
            for (Entry<Integer, OpcodeAndOperand> entry : codeMap.entrySet()) {
                OpcodeAndOperand oa = entry.getValue();
                sb.append(oa.getOpcode());
                if (oa.getOperand() != null) {
                    sb.append(" ").append(oa.getOperand());
                }
                sb.append(" ");
            }
            sb.append("\n");
        } else {
            // 多行方法体
            for (Entry<Integer, Integer> lineEntry : lineMap.entrySet()) {
                int javaLine = lineEntry.getKey();
                int bytecodeStart = lineEntry.getValue();

                sb.append("\t\t// line ").append(javaLine).append(": ");
                for (Entry<Integer, OpcodeAndOperand> entry : codeMap.entrySet()) {
                    if (entry.getKey() >= bytecodeStart) {
                        OpcodeAndOperand oa = entry.getValue();
                        sb.append(oa.getOpcode());
                        if (oa.getOperand() != null) {
                            sb.append(" ").append(oa.getOperand());
                        }
                        sb.append(" ");
                    }
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * 收集局部变量信息
     */
    private static void collectLocalVariables(Code codeAttr, ClassFile classFile,
                                            Map<Integer, FieldNameAndType> variableNameMap) {
        if (codeAttr.getAttributes() == null) {
            return;
        }

        List<AbstractConstantPool> poolList = classFile.poolInfo.getPoolList();

        for (AbstractAttribute attr : codeAttr.getAttributes()) {
            if (!(attr instanceof LocalVariableTable)) {
                continue;
            }

            LocalVariableTable lvt = (LocalVariableTable) attr;
            for (LocalVariableTable.LocalVariableInfo lvi : lvt.getLocalVariableTable()) {
                int slotIndex = lvi.getIndex().value;
                int nameIdx = lvi.getNameIndex().value - 1;
                int descIdx = lvi.getDescriptorIndex().value - 1;

                String name = getUtf8String(poolList, nameIdx);
                String type = getUtf8String(poolList, descIdx);

                FieldNameAndType fnt = new FieldNameAndType();
                fnt.setName(name);
                fnt.setType(type);
                variableNameMap.put(slotIndex, fnt);
            }
        }
    }

    /**
     * 收集行号信息
     */
    private static void collectLineNumbers(Code codeAttr, ClassFile classFile,
                                         TreeMap<Integer, Integer> lineMap) {
        if (codeAttr.getAttributes() == null) {
            return;
        }

        for (AbstractAttribute attr : codeAttr.getAttributes()) {
            if (!(attr instanceof LineNumberTable)) {
                continue;
            }

            LineNumberTable lnt = (LineNumberTable) attr;
            for (LineNumberTable.LineNumberInfo lni : lnt.getLineNumberTable()) {
                int javaLine = lni.getLineNumber().value;
                int bytecodeLine = lni.getStartPc().value;
                lineMap.put(javaLine, bytecodeLine);
            }
        }
    }

    /**
     * 解码字节码为 opcode 和操作数
     */
    private static Map<Integer, OpcodeAndOperand> decodeBytecode(List<U1> codeBytes) {
        Map<Integer, OpcodeAndOperand> codeMap = new LinkedHashMap<>();

        int pc = 0;
        while (pc < codeBytes.size()) {
            int opcode = codeBytes.get(pc).value & 0xFF;
            OpcodeAndOperand oa = new OpcodeAndOperand();

            String hexCode = String.format("%02x", opcode);
            String opcodeName = CodeConvertor.codeConvertor(hexCode);

            if (opcodeName == null) {
                opcodeName = "unknown_" + hexCode;
            }
            oa.setOpcode(opcodeName);

            int opcodePc = pc;
            pc++;

            // 判断操作数字节数
            int operandBytes = OperandBytesJudge.operandBytesCount(opcodeName);
            if (operandBytes > 0 && pc + operandBytes <= codeBytes.size()) {
                StringBuilder operand = new StringBuilder();
                for (int j = 0; j < operandBytes; j++) {
                    operand.append(String.format("%02x", codeBytes.get(pc).value));
                    pc++;
                }
                try {
                    oa.setOperand(Integer.parseInt(operand.toString(), 16));
                } catch (NumberFormatException e) {
                    oa.setOperand(operand.toString());
                }
            }

            codeMap.put(opcodePc, oa);
        }

        return codeMap;
    }

    /**
     * 获取常量池中的 UTF8 字符串
     */
    private static String getUtf8String(List<AbstractConstantPool> poolList, int index) {
        if (index < 0 || index >= poolList.size()) {
            return "";
        }
        AbstractConstantPool pool = poolList.get(index);
        if (pool instanceof Utf8Info) {
            return ((Utf8Info) pool).getValue();
        }
        return "";
    }

    /**
     * 拼接方法返回类型、方法名和参数
     */
    private static String jointMethodReturnNameParams(String methodName, String descriptor) {
        // descriptor 格式: (参数类型...)返回类型
        int parenOpen = descriptor.indexOf('(');
        int parenClose = descriptor.indexOf(')');

        if (parenOpen == -1 || parenClose == -1) {
            return methodName + " " + descriptor;
        }

        String params = descriptor.substring(parenOpen + 1, parenClose);
        String returnType = descriptor.substring(parenClose + 1);

        String javaParams = ParamsConvertor.paramsConvertorMethodParams(params);
        String javaReturnType = ParamsConvertor.paramsConvertorMethodReturnType(returnType);

        return javaReturnType + " " + methodName + "(" + javaParams + ")";
    }

    /**
     * 处理异常声明
     */
    private static String throwExceptionsJudge(MethodInfo methodInfo, ClassFile classFile) {
        // TODO: 实现异常处理
        return "";
    }

    // ==================== 内部类 ====================

    /**
     * 操作码和操作数
     */
    private static class OpcodeAndOperand {
        private String opcode;
        private Object operand;

        public String getOpcode() { return opcode; }
        public void setOpcode(String opcode) { this.opcode = opcode; }
        public Object getOperand() { return operand; }
        public void setOperand(Object operand) { this.operand = operand; }
    }

    /**
     * 字段名和类型
     */
    private static class FieldNameAndType {
        private String name;
        private String type;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
}
