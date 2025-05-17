package com.example.demo.thread;

import java.util.LinkedList;
import java.util.Queue;

class SharedResource {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 5;

    public synchronized void produce(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            System.out.println("队列已满，生产者等待...");
            wait();
        }
        queue.add(item);
        System.out.println("生产者生产了: " + item);
        notifyAll(); // 唤醒所有等待的消费者线程
    }

    public synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("队列为空，消费者等待...");
            wait();
        }
        int item = queue.poll();
        System.out.println("消费者消费了: " + item);
        notifyAll(); // 唤醒所有等待的生产者线程
        return item;
    }
}