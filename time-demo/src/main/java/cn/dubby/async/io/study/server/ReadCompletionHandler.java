package cn.dubby.async.io.study.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Date;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private static final Logger logger = LoggerFactory.getLogger(ReadCompletionHandler.class);

    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel) {
        this.channel = asynchronousSocketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer byteBuffer) {
        byteBuffer.flip();
        byte[] body = new byte[byteBuffer.remaining()];
        byteBuffer.get(body);
        String request = new String(body, Charset.defaultCharset());
        logger.info("request {}", request);
        String currentTime = new Date().toString();
        doWrite(currentTime);

        byteBuffer.clear();
        channel.read(byteBuffer, byteBuffer, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error("read failed");
    }

    private void doWrite(String data) {
        byte[] dataBytes = data.getBytes(Charset.defaultCharset());
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataBytes.length);
        byteBuffer.put(dataBytes);
        byteBuffer.flip();

        channel.write(byteBuffer, byteBuffer, new WriteCompletionHandler(channel));
    }
}
