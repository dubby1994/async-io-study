package cn.dubby.async.io.server.test.server;

import cn.dubby.async.io.server.Server;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dubby
 * @date 2019/1/22 22:36
 */
public class ServerTest {

    @Test
    public void start() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        new Server(8080, executorService).start();

        new CountDownLatch(1).await();
    }

}
