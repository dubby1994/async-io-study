package cn.dubby.async.io.study.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}