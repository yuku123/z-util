package com.zifang.util.proxy.a.model.attribute;

import com.zifang.util.proxy.a.model.readtype.U2;
import com.zifang.util.proxy.a.model.readtype.U4;

import java.io.InputStream;

/**
 * 源码文件属性
 * <p>
 * SourceFile_attribute用于指明源文件的文件名。
 * 属于可选属性，如果不存在则调试时无法显示源码文件名。
 */
public class SourceFile extends AbstractAttribute {
    private U2 sourceFileIndex;//java源文件名称


    public SourceFile(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {

    }

    public U2 getSourceFileIndex() {
        return sourceFileIndex;
    }

    public void setSourceFileIndex(U2 sourceFileIndex) {
        this.sourceFileIndex = sourceFileIndex;
    }
}
