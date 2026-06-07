package com.zifang.util.core.parser.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * XmlTransformer类。
 */
/**
 * XmlTransformer类。
 */
public class XmlTransformer {


    /**
     * XML转换为javabean
     * Jul 25, 2013 9:39:05 PM xuejiangtao添加此方法
     *
     * @param <T>
     * @param xml
     * @param t
     * @return
     * @throws JAXBException
     */
    /**
     * xmlToBean方法。
     *      * @param xml String类型参数
     * @param t T类型参数
     * @return static <T> T类型返回值
     */
    /**
     * xmlToBean方法。
     *      * @param xml String类型参数
     * @param t T类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T xmlToBean(String xml, T t) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Unmarshaller um = context.createUnmarshaller();
        StringReader sr = new StringReader(xml);
        t = (T) um.unmarshal(sr);
        return t;
    }

    /**
     * javabean转换为XML
     * Jul 25, 2013 9:39:09 PM xuejiangtao添加此方法
     *
     * @return
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    /**
     * beanToXml方法。
     *      * @param t T类型参数
     * @return static <T> StringWriter类型返回值
     */
    /**
     * beanToXml方法。
     *      * @param t T类型参数
     * @return static <T> StringWriter类型返回值
     */
    public static <T> StringWriter beanToXml(T t) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Marshaller m = context.createMarshaller();
        StringWriter sw = new StringWriter();
        m.marshal(t, sw);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//是否格式化
        m.marshal(t, new FileOutputStream("test.xml"));
        return sw;
    }

}
