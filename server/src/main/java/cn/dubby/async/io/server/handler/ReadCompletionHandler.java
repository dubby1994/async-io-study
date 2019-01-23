package cn.dubby.async.io.server.handler;

import cn.dubby.async.io.server.constant.MessageConstant;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static cn.dubby.async.io.server.constant.MessageConstant.DefaultCharset;

/**
 * 这个类是有状态的，因为包含一个变量channel，每个连接都需要用自己的instance
 *
 * @author dubby
 * @date 2019/1/22 10:25
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private static final Logger logger = LoggerFactory.getLogger(ReadCompletionHandler.class);

    /**
     * IO处理完执行业务逻辑的线程池
     */
    private ExecutorService businessThreadPool;

    private AsynchronousSocketChannel channel;

    private ByteOutputStream stashBuffer;

    public ReadCompletionHandler(ExecutorService businessThreadPool, AsynchronousSocketChannel channel) {
        this.businessThreadPool = businessThreadPool;
        this.channel = channel;
    }

    @Override
    public void completed(Integer readLength, ByteBuffer byteBuffer) {
        if (readLength < 0) {
            try {
                channel.close();
            } catch (Exception e) {
                logger.error("channel close failed", e);
            }
            return;
        }

        if (readLength == 0) {
            logger.info("receive empty message");
            return;
        }

        byteBuffer.flip();

        List<ByteOutputStream> outputStreamList = new ArrayList<>();

        if (stashBuffer == null) {
            stashBuffer = new ByteOutputStream(1024);
        }

        for (int i = 0; i < readLength; ++i) {
            byte b = byteBuffer.get();
            if (b == MessageConstant.Separator) {
                outputStreamList.add(stashBuffer);
                stashBuffer = new ByteOutputStream(1024);
            } else {
                stashBuffer.write(b);
            }
        }
        byteBuffer.clear();

        channel.read(byteBuffer, byteBuffer, this);

        businessThreadPool.submit(() -> {
            outputStreamList.stream().map(o -> new String(o.getBytes(), 0, o.getCount(), DefaultCharset)).forEach(this::doWrite);
        });
    }

    @Override
    public void failed(Throwable throwable, ByteBuffer attachment) {
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {
                logger.error("channel.close failed", throwable);
            }
        }
        logger.error("read failed", throwable);
    }

    private void doWrite(String data) {
        logger.info("request {}", data);
        byte[] dataBytes = ("result:" + data + "\n").getBytes(Charset.defaultCharset());
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataBytes.length);
        byteBuffer.put(dataBytes);
        byteBuffer.flip();
        channel.write(byteBuffer, byteBuffer, new WriteCompletionHandler(businessThreadPool, channel));
    }
}
