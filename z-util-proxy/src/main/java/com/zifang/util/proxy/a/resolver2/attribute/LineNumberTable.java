package com.zifang.util.proxy.a.resolver2.attribute;

import com.zifang.util.proxy.a.resolver2.readtype.U2;
import com.zifang.util.proxy.a.resolver2.readtype.U4;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 行号表属性
 * <p>
 * LineNumberTable_attribute用于关联字节码偏移和源码行号。
 * 用于调试时提供堆栈跟踪的源码位置信息。
 */
public class LineNumberTable extends AbstractAttribute {
    private U2 lineNumTableLength;
    private List<LineNumberInfo> lineNumberTable = new ArrayList<>();

    public LineNumberTable(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        lineNumTableLength = U2.read(inputStream);
        short value = lineNumTableLength.value;
        while (value > 0) {
            LineNumberInfo lineNumberInfo = new LineNumberInfo(U2.read(inputStream), U2.read(inputStream));
            lineNumberTable.add(lineNumberInfo);
            value--;
        }
    }


    public class LineNumberInfo {
        private U2 startPc;//字节码行号
        private U2 lineNumber;//java源码行号

        public LineNumberInfo(U2 startPc, U2 lineNumber) {
            this.startPc = startPc;
            this.lineNumber = lineNumber;
        }

        public U2 getStartPc() {
            return startPc;
        }

        public void setStartPc(U2 startPc) {
            this.startPc = startPc;
        }

        public U2 getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(U2 lineNumber) {
            this.lineNumber = lineNumber;
        }
    }
}
