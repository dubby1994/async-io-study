package cn.dubby.async.io.server.test.util;

import cn.dubby.async.io.server.constant.MessageConstant;
import cn.dubby.async.io.server.util.ByteConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author dubby
 * @date 2019/1/22 21:42
 */
public class ByteUtilTest {

    @Test
    public void test() {
        //这里测试10G，一般来说，一次传输的大小不会超过10G
        for (int i = -1024 * 1024 * 10; i <= 1024 * 1024 * 10; ++i) {
            byte[] bytes = ByteConverter.convert(i);
            int result = ByteConverter.convert(bytes);
            Assert.assertEquals(i, result);
        }
    }

    @Test
    public void testSeparator() {
        String s1 = new String(new byte[]{MessageConstant.Separator}, MessageConstant.DefaultCharset);
        System.out.println(s1);

        String s2 = " ";
        System.out.println(s2);
    }

}
