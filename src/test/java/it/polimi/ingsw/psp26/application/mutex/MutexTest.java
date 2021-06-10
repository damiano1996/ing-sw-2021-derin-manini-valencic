package it.polimi.ingsw.psp26.application.mutex;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class MutexTest {

    private Mutex mutex;
    private double startT1, endT1, startT2, endT2;

    @Before
    public void setUp() throws Exception {
        mutex = new Mutex();
    }


    @Test
    public void testLockAndUnlock() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            mutex.lock();
            startT1 = System.currentTimeMillis();
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            endT1 = System.currentTimeMillis();
            mutex.unlock();
        });

        Thread t2 = new Thread(() -> {
            mutex.lock();
            startT2 = System.currentTimeMillis();
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            endT2 = System.currentTimeMillis();
            mutex.unlock();
        });

        t1.start();
        t2.start();

        TimeUnit.MILLISECONDS.sleep(500);

        t1.join();
        t2.join();
        
        assertTrue(
                (startT1 < startT2 && endT1 <= startT2) ||
                        (startT2 < startT1 && endT2 <= startT1)
        );
    }

}