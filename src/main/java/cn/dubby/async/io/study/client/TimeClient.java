package cn.dubby.async.io.study.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

public class TimeClient {

    private static final Logger logger = LoggerFactory.getLogger(TimeClient.class);

    public static void main(String[] args) {
        try {
            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
            channel.connect(new InetSocketAddress("127.0.0.1", 8080),
                    System.currentTimeMillis(),
                    new CompletionHandler<Void, Long>() {
                        @Override
                        public void completed(Void result, Long attachment) {
                            String request = "PING";
                            byte[] bytes = request.getBytes(Charset.defaultCharset());
                            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                            writeBuffer.put(bytes);
                            writeBuffer.flip();
                            channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    if (attachment.hasRemaining()) {
                                        channel.write(attachment, attachment, this);
                                    } else {
                                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                                        channel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                                            @Override
                                            public void completed(Integer result, ByteBuffer attachment) {
                                                attachment.flip();
                                                byte[] bytes1 = new byte[attachment.remaining()];
                                                attachment.get(bytes1);
                                                String body = new String(bytes1, Charset.defaultCharset());
                                                logger.info("response {}", body);
                                            }

                                            @Override
                                            public void failed(Throwable exc, ByteBuffer attachment) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {

                                }
                            });
                        }

                        @Override
                        public void failed(Throwable exc, Long attachment) {

                        }
                    });

            new CountDownLatch(1).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
