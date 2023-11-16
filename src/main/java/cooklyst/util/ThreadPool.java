package cooklyst.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(10,10,1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }
}
