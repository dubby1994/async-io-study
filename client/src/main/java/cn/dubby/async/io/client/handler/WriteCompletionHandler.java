package cn.dubby.async.io.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

/**
 * 这个类是有状态的，因为包含一个变量channel，每个连接都需要用自己的instance
 *
 * @author dubby
 * @date 2019/1/22 10:25
 */
public class WriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private static final Logger logger = LoggerFactory.getLogger(WriteCompletionHandler.class);

    private AsynchronousSocketChannel channel;

    public WriteCompletionHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (attachment.hasRemaining()) {
            channel.write(attachment, attachment, this);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error("write failed");
    }
}
