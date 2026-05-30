package com.zifang.util.office.word;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.toc.TocGenerator;

/**
 * Word文档目录更新示例类
 * 演示如何使用docx4j库更新Word文档的目录
 */
/**
 * TocUpdateDemo类。
 */
public class TocUpdateDemo {

    static boolean update = false;

    /**
     * main方法。
     *      * @param args String[]类型参数
     * @return static void类型返回值
     */
    public static void main(String[] args) throws Exception {

        String input_DOCX = "/Users/zifang/Downloads/test.docx";

        // Load input_template.docx
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(
                new java.io.File(input_DOCX));
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();


        TocGenerator tocGenerator = new TocGenerator(wordMLPackage);

//        tocGenerator.setDocumentServicesEndpoint("http://192.168.2.16:9015/v1/00000000-0000-0000-0000-000000000000/convert");

//        	Toc.setTocHeadingText("Sumário");
        tocGenerator.updateToc(false); // true --> skip page numbering; its currently much faster

        wordMLPackage.save(new java.io.File("/Users/zifang/Downloads/test_out.docx"));

    }


}