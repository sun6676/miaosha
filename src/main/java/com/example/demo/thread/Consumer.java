package com.example.demo.thread;

class Consumer extends Thread {
    private final SharedResource sharedResource;

    public Consumer(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                sharedResource.consume();
                Thread.sleep((long) (Math.random() * 1000)); // 模拟消费时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}