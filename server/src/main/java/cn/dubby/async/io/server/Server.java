package cn.dubby.async.io.server;

import cn.dubby.async.io.server.handler.AcceptCompletionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dubby
 * @date 2019/1/22 15:32
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(AcceptCompletionHandler.class);

    private int port;

    /**
     * IO处理完执行业务逻辑的线程池
     */
    private ExecutorService businessThreadPool;

    public Server(int port, ExecutorService businessThreadPool) {
        this.port = port;
        this.businessThreadPool = businessThreadPool;
    }

    public void start() throws IOException {
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 64 * 1024);
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.accept(serverSocketChannel, new AcceptCompletionHandler(businessThreadPool));
        logger.info("server started, port:{}", port);
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            new Server(8080, executorService).start();
            new CountDownLatch(1).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
