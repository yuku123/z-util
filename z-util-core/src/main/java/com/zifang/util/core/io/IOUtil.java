package com.zifang.util.core.io;

import com.zifang.util.core.lang.StringUtil;

import java.io.*;

/**
 * @author zifang
 */
public class IOUtil {

    public static final int EOF = -1;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * 从输入流中读取内容并转换为字符串
     *
     * @param in          输入流
     * @param charsetName 字符编码，为空则使用默认编码
     * @return 读取到的字符串内容
     * @throws IOException 如果读取发生异常
     */
    public static String read(InputStream in, String charsetName) throws IOException {
        FastByteArrayOutputStream out = read(in);
        return StringUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
    }


    /**
     * 从流中读取内容，读到输出流中，读取完毕后并不关闭流
     */
    public static FastByteArrayOutputStream read(InputStream in) throws IOException {
        return read(in, true);
    }

    /**
     * 从流中读取内容，读到输出流中，读取完毕后并不关闭流
     */
    public static FastByteArrayOutputStream read(InputStream in, boolean isClose) throws IOException {
        final FastByteArrayOutputStream out;
        if (in instanceof FileInputStream) {
            // 文件流的长度是可预见的，此时直接读取效率更高
            try {
                out = new FastByteArrayOutputStream(in.available());
            } catch (IOException e) {
                throw e;
            }
        } else {
            out = new FastByteArrayOutputStream();
        }
        try {
            copy(in, out);
        } finally {
            if (isClose) {
                close(in);
            }
        }
        return out;
    }

    /**
     * 关闭指定的 Closeable 对象
     *
     * @param closeable 要关闭的可关闭对象，如果为 null 则忽略
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//
//    public static byte[] read(InputStream inputStream) throws IOException {
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int num = inputStream.read(buffer);
//            while (num != -1) {
//                baos.write(buffer, 0, num);
//                num = inputStream.read(buffer);
//            }
//            baos.flush();
//            return baos.toByteArray();
//        } finally {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//        }
//    }


    /**
     * 将输入流的内容复制到输出流
     *
     * @param input  输入流
     * @param output 输出流
     * @return 复制的字节数，如果超过 Integer.MAX_VALUE 则返回 -1
     * @throws IOException 如果读取或写入发生异常
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * 将输入流的内容大规模复制到输出流，使用较大的缓冲区
     *
     * @param input  输入流
     * @param output 输出流
     * @return 复制的总字节数
     * @throws IOException 如果读取或写入发生异常
     */
    public static long copyLarge(InputStream input, OutputStream output) throws IOException {

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
