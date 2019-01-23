package cn.dubby.async.io.client.handler;

import cn.dubby.async.io.client.constant.MessageConstant;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Arrays;
import java.util.List;

/**
 * @author dubby
 * @date 2019/1/23 17:31
 */
public class ConnectedCompletionHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {

    private static final Logger logger = LoggerFactory.getLogger(ConnectedCompletionHandler.class);

    @Override
    public void completed(Void result, AsynchronousSocketChannel channel) {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        channel.read(readBuffer, readBuffer, new ReadCompletionHandler(channel));

        List<String> requestList = Arrays.asList("123", "hello", "dubby", "xyz", "yangzheng");

        ByteOutputStream byteOutputStream = new ByteOutputStream(1024);
        for (String s : requestList) {
            byteOutputStream.write(s.getBytes(MessageConstant.DefaultCharset));
            byteOutputStream.write(MessageConstant.Separator);
        }

        ByteBuffer writeBuffer = ByteBuffer.allocate(byteOutputStream.getCount());
        writeBuffer.put(byteOutputStream.getBytes(), 0, byteOutputStream.getCount());
        writeBuffer.flip();
        channel.write(writeBuffer, writeBuffer, new WriteCompletionHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {
                logger.error("channel.close failed", exc);
            }
        }
        logger.error("connect broken", exc);
    }
}
