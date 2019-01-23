package cn.dubby.async.io.server.util;

/**
 * @author dubby
 * @date 2019/1/22 21:39
 */
public class ByteConverter {

    public static int convert(byte... bytes) {
        int result = 0;
        for (byte b : bytes) {
            result = b & 0x0FF | result << 8;
        }
        return result;
    }


    public static byte[] convert(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i >> (3 * 8));
        bytes[1] = (byte) ((i & 0x00ff0000) >> 16);
        bytes[2] = (byte) ((i & 0x0000ff00) >> 8);
        bytes[3] = (byte) (i & 0x000000ff);
        return bytes;
    }
}
