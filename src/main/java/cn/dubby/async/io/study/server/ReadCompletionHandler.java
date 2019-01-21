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
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        String request = new String(body, Charset.defaultCharset());
        logger.info("request {}", request);
        String currentTime = new Date().toString();
        doWrite(currentTime);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error("ReadCompletionHandler failed");
    }

    private void doWrite(String data) {
        byte[] dataBytes = data.getBytes(Charset.defaultCharset());
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataBytes.length);
        byteBuffer.put(dataBytes);
        byteBuffer.flip();

        channel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (attachment.hasRemaining()) {
                    channel.write(attachment, attachment, this);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                logger.error("doWrite failed");
            }
        });
    }
}
