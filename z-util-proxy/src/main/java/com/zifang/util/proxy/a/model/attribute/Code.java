package com.zifang.util.proxy.a.model.attribute;

import com.zifang.util.proxy.a.model.constantpool.AbstractConstantPool;
import com.zifang.util.proxy.a.model.readtype.U1;
import com.zifang.util.proxy.a.model.readtype.U2;
import com.zifang.util.proxy.a.model.readtype.U4;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Code属性
 * <p>
 * 方法的字节码指令存储在这个属性中。
 */
public class Code extends AbstractAttribute {
    private U2 maxStack;
    private U2 maxLocals;
    private U4 codeLength;
    private List<U1> code = new ArrayList<>();
    private U2 exceptionTableLength;
    private List<ExceptionInfo> exceptionTable = new ArrayList<>();
    private U2 attributesCount;
    private List<AbstractAttribute> attributes = new ArrayList<>();
    private List<AbstractConstantPool> poolList;

    public Code(U2 attributeNameIndex, U4 attributeLength, List<AbstractConstantPool> poolList) {
        super(attributeNameIndex, attributeLength);
        this.poolList = poolList;
    }

    @Override
    public void read(InputStream inputStream) {
        maxStack = U2.read(inputStream);
        maxLocals = U2.read(inputStream);
        codeLength = U4.read(inputStream);
        int value = codeLength.value;
        while (value > 0) {
            code.add(U1.read(inputStream));
            value--;
        }
        exceptionTableLength = U2.read(inputStream);
        short exLength = exceptionTableLength.value;
        while (exLength > 0) {
            ExceptionInfo exceptionInfo = new ExceptionInfo(
                    U2.read(inputStream),
                    U2.read(inputStream),
                    U2.read(inputStream),
                    U2.read(inputStream)
            );
            exceptionTable.add(exceptionInfo);
            exLength--;
        }
        attributesCount = U2.read(inputStream);
        short attrCount = attributesCount.value;
        while (attrCount > 0) {
            AbstractAttribute attributeTable = AttributeFactory.getAttributeTable(inputStream, poolList);
            if (attributeTable != null) {
                attributes.add(attributeTable);
            }
            attrCount--;
        }
    }

    /**
     * 异常表项
     */
    public class ExceptionInfo {
        private U2 startPc;
        private U2 endPc;
        private U2 handlerPc;
        private U2 catchPc;

        public ExceptionInfo(U2 startPc, U2 endPc, U2 handlerPc, U2 catchPc) {
            this.startPc = startPc;
            this.endPc = endPc;
            this.handlerPc = handlerPc;
            this.catchPc = catchPc;
        }

        public U2 getStartPc() {
            return startPc;
        }

        public U2 getEndPc() {
            return endPc;
        }

        public U2 getHandlerPc() {
            return handlerPc;
        }

        public U2 getCatchPc() {
            return catchPc;
        }
    }

    public U2 getMaxStack() {
        return maxStack;
    }

    public U2 getMaxLocals() {
        return maxLocals;
    }

    public U4 getCodeLength() {
        return codeLength;
    }

    public List<U1> getCode() {
        return code;
    }

    public U2 getExceptionTableLength() {
        return exceptionTableLength;
    }

    public List<ExceptionInfo> getExceptionTable() {
        return exceptionTable;
    }

    public U2 getAttributesCount() {
        return attributesCount;
    }

    public List<AbstractAttribute> getAttributes() {
        return attributes;
    }
}
