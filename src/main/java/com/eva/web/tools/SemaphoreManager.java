package com.eva.web.tools;

import java.util.concurrent.Semaphore;

public class SemaphoreManager {

    private Semaphore semaphore;

    public SemaphoreManager(int permits) {
        semaphore = new Semaphore(permits, true);
    }

    public void acquire() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        semaphore.release();
    }
}
