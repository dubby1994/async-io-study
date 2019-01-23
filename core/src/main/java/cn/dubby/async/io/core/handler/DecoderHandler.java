package cn.dubby.async.io.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;

/**
 * 这个类是有状态的，因为包含一个变量channel，每个连接都需要用自己的instance
 *
 * @author dubby
 * @date 2019/1/23 16:49
 */
public class DecoderHandler {

    private static final Logger logger = LoggerFactory.getLogger(DecoderHandler.class);

    /**
     * IO处理完执行业务逻辑的线程池
     */
    private ExecutorService businessThreadPool;

    private AsynchronousSocketChannel channel;

    private BusinessHandler businessHandler;

    public DecoderHandler(ExecutorService businessThreadPool, AsynchronousSocketChannel channel, BusinessHandler businessHandler) {
        this.businessThreadPool = businessThreadPool;
        this.channel = channel;
        this.businessHandler = businessHandler;
    }

    public void appendMessage(ByteBuffer byteBuffer) {

    }
}
