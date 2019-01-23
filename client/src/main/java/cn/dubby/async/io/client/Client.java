package cn.dubby.async.io.client;

import cn.dubby.async.io.client.handler.ConnectedCompletionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        try {
            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
            channel.connect(new InetSocketAddress("127.0.0.1", 8080), channel, new ConnectedCompletionHandler());

            new CountDownLatch(1).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
