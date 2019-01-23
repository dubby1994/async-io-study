package cn.dubby.async.io.core.constant;

import java.nio.charset.Charset;

/**
 * @author dubby
 * @date 2019/1/22 20:03
 */
public class MessageConstant {

    public static final byte MAGIC_NUMBER_1 = (byte) 0xD0;

    public static final byte MAGIC_NUMBER_2 = (byte) 0x0D;

    public static final byte VERSION_1 = (byte) 0x00;

    public static final byte VERSION_2 = (byte) 0x00;

    public static final Charset DefaultCharset = Charset.forName("UTF-8");

    public static final int READ_BUFFER_SIZE = Integer.getInteger("read.buffer.size", 2048);
}
