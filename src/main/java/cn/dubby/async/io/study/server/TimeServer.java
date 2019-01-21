package cn.dubby.async.io.study.server;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author dubby
 * @date 2019/1/21 22:04
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        try {
            AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            asynchronousServerSocketChannel.accept(asynchronousServerSocketChannel, new AcceptCompletionHandler());

            new CountDownLatch(1).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}