package cn.dubby.async.io.study.server;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author dubby
 * @date 2019/1/21 22:12
 */
public class ReadCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, String> {

    @Override
    public void completed(AsynchronousSocketChannel result, String attachment) {

    }

    @Override
    public void failed(Throwable exc, String attachment) {

    }
}
