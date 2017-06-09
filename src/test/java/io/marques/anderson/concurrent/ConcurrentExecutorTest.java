package io.marques.anderson.concurrent;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * Created by anderson on 09/06/17.
 */
public class ConcurrentExecutorTest {

    int value = 0;

    @Test
    public void shouldIncrementTo10(){
        ConcurrentExecutor ce = new ConcurrentExecutor();
        ce.requests(10);
        ce.threads(10);
        ce.timeoutMillis(1000);
        ce.execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                value++;
                return null;
            }
        });

        Assert.assertTrue(value == 10);
    }

    @Test
    public void shouldNotReachTheEnd(){
        ConcurrentExecutor ce = new ConcurrentExecutor();
        ce.requests(10);
        ce.threads(10);
        ce.timeoutMillis(1000);
        ce.execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Thread.sleep(1000);
                return null;
            }
        });
        System.out.println(value);
        Assert.assertTrue(value < 10);
    }
}
