package com.example.demo.thread;

class Producer extends Thread {
    private final SharedResource sharedResource;

    public Producer(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                sharedResource.produce(i);
                Thread.sleep((long) (Math.random() * 1000)); // 模拟生产时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}