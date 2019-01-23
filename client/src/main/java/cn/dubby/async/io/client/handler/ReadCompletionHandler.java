package cn.dubby.async.io.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

/**
 * @author dubby
 * @date 2019/1/23 19:09
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private static final Logger logger = LoggerFactory.getLogger(ReadCompletionHandler.class);

    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (result < 0) {
            return;
        }

        if (result == 0) {
            return;
        }
        attachment.flip();
        byte[] bytes1 = new byte[attachment.remaining()];
        attachment.get(bytes1);
        String body = new String(bytes1, Charset.defaultCharset());
        logger.info("response {}", body);

        attachment.clear();

        channel.read(attachment, attachment, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error("failed", exc);
    }
}
