package cn.dubby.async.io.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;

/**
 * 这个类是有状态的，因为包含一个变量channel，每个连接都需要用自己的instance
 *
 * @author dubby
 * @date 2019/1/22 10:25
 */
public class WriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private static final Logger logger = LoggerFactory.getLogger(WriteCompletionHandler.class);

    /**
     * IO处理完执行业务逻辑的线程池
     */
    private ExecutorService businessThreadPool;

    private AsynchronousSocketChannel channel;

    public WriteCompletionHandler(ExecutorService businessThreadPool, AsynchronousSocketChannel channel) {
        this.businessThreadPool = businessThreadPool;
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (attachment.hasRemaining()) {
            channel.write(attachment, attachment, this);
        }
        logger.info("write completed");
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.error("write failed");
    }
}
