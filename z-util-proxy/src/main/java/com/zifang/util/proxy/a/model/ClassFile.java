package com.zifang.util.proxy.a.model;

import com.zifang.util.proxy.a.model.constantpool.ClassInfo;
import com.zifang.util.proxy.a.model.constantpool.ConstantPoolInfo;
import com.zifang.util.proxy.a.model.constantpool.AbstractConstantPool;
import com.zifang.util.proxy.a.model.constantpool.Utf8Info;
import com.zifang.util.proxy.a.model.field.FieldTable;
import com.zifang.util.proxy.a.model.inter.InterfaceIndex;
import com.zifang.util.proxy.a.model.method.MethodTable;
import com.zifang.util.proxy.a.model.readtype.U2;
import com.zifang.util.proxy.a.model.readtype.U4;

/**
 * ClassFile结构
 * <p>
 * 代表一个完整的.class文件结构，遵循JVM规范。
 */
public class ClassFile {
    /** 魔数，固定为0xCAFEBABE */
    public U4 magic;

    /** 次版本号 */
    public U2 minorVersion;

    /** 主版本号 (52=Java8, 55=Java11, 61=Java17) */
    public U2 majorVersion;

    /** 常量池大小 */
    public U2 constantPoolSize;

    /** 常量池内容 */
    public ConstantPoolInfo poolInfo;

    /** 类的访问标志 (ACC_PUBLIC, ACC_FINAL等) */
    public U2 accessFlag;

    /** 类索引，指向常量池中的CONSTANT_Class_info */
    public U2 classIndex;

    /** 父类索引，指向常量池中的CONSTANT_Class_info */
    public U2 superClassIndex;

    /** 接口索引表 */
    public InterfaceIndex interfaceIndex;

    /** 字段表 */
    public FieldTable fieldInfo;

    /** 方法表 */
    public MethodTable methodInfo;

    /**
     * 获取类名
     *
     * @return 类名字符串
     */
    public String getClassName() {
        if (poolInfo == null || poolInfo.getPoolList() == null || classIndex == null) {
            return null;
        }
        // classIndex 指向常量池中的 ClassInfo (tag=7)
        int classInfoIndex = classIndex.value - 1;
        if (classInfoIndex < 0 || classInfoIndex >= poolInfo.getPoolList().size()) {
            return null;
        }
        AbstractConstantPool classInfo = poolInfo.getPoolList().get(classInfoIndex);
        if (!(classInfo instanceof ClassInfo)) {
            return null;
        }
        // ClassInfo.nameIndex 指向 Utf8Info
        int utf8Index = ((ClassInfo) classInfo).getNameIndex().value - 1;
        if (utf8Index < 0 || utf8Index >= poolInfo.getPoolList().size()) {
            return null;
        }
        AbstractConstantPool utf8Pool = poolInfo.getPoolList().get(utf8Index);
        if (utf8Pool instanceof Utf8Info) {
            return ((Utf8Info) utf8Pool).getValue();
        }
        return null;
    }

    /**
     * 获取方法数量
     *
     * @return 方法数量
     */
    public int getMethodCount() {
        if (methodInfo == null) {
            return 0;
        }
        return methodInfo.getMethodCount();
    }

    @Override
    public String toString() {
        return "ClassFile{" +
                "magic=" + (magic != null ? String.format("0x%08X", magic.value) : "null") +
                ", majorVersion=" + (majorVersion != null ? majorVersion.value : 0) +
                ", minorVersion=" + (minorVersion != null ? minorVersion.value : 0) +
                ", constantPoolSize=" + (constantPoolSize != null ? constantPoolSize.value : 0) +
                '}';
    }


    public U4 getMagic() {
        return magic;
    }

    public void setMagic(U4 magic) {
        this.magic = magic;
    }

    public U2 getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(U2 minorVersion) {
        this.minorVersion = minorVersion;
    }

    public U2 getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(U2 majorVersion) {
        this.majorVersion = majorVersion;
    }

    public U2 getConstantPoolSize() {
        return constantPoolSize;
    }

    public void setConstantPoolSize(U2 constantPoolSize) {
        this.constantPoolSize = constantPoolSize;
    }

    public ConstantPoolInfo getPoolInfo() {
        return poolInfo;
    }

    public void setPoolInfo(ConstantPoolInfo poolInfo) {
        this.poolInfo = poolInfo;
    }

    public U2 getAccessFlag() {
        return accessFlag;
    }

    public void setAccessFlag(U2 accessFlag) {
        this.accessFlag = accessFlag;
    }

    public U2 getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(U2 classIndex) {
        this.classIndex = classIndex;
    }

    public U2 getSuperClassIndex() {
        return superClassIndex;
    }

    public void setSuperClassIndex(U2 superClassIndex) {
        this.superClassIndex = superClassIndex;
    }

    public InterfaceIndex getInterfaceIndex() {
        return interfaceIndex;
    }

    public void setInterfaceIndex(InterfaceIndex interfaceIndex) {
        this.interfaceIndex = interfaceIndex;
    }

    public FieldTable getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(FieldTable fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public MethodTable getMethodInfo() {
        return methodInfo;
    }

    public void setMethodInfo(MethodTable methodInfo) {
        this.methodInfo = methodInfo;
    }
}
