package cn.dubby.async.io.client.constant;

import java.nio.charset.Charset;

/**
 * @author dubby
 * @date 2019/1/22 20:03
 */
public class MessageConstant {

    public static final byte Separator = 0x00;

    public static final Charset DefaultCharset = Charset.forName("UTF-8");

    public static final int READ_BUFFER_SIZE = Integer.getInteger("read.buffer.size", 2048);
}
