package cn.dubby.async.io.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;

/**
 * 这个类是无状态的，可以共用一个instance
 *
 * @author dubby
 * @date 2019/1/22 10:25
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private static final Logger logger = LoggerFactory.getLogger(AcceptCompletionHandler.class);

    /**
     * IO处理完执行业务逻辑的线程池
     */
    private ExecutorService businessThreadPool;

    public AcceptCompletionHandler(ExecutorService businessThreadPool) {
        this.businessThreadPool = businessThreadPool;
    }

    @Override
    public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel serverSocketChannel) {
        //继续accept下一个连接
        serverSocketChannel.accept(serverSocketChannel, this);

        ByteBuffer buffer = ByteBuffer.allocate(4);
        result.read(buffer, buffer, new ReadCompletionHandler(businessThreadPool, result));
    }

    @Override
    public void failed(Throwable throwable, AsynchronousServerSocketChannel attachment) {
        logger.info("server accept failed", throwable);
        throwable.fillInStackTrace();
    }
}
