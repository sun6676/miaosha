package com.example.demo.thread;

public class SynchronizedProducerConsumerExample {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();

        Thread producerThread = new Producer(sharedResource);
        Thread consumerThread = new Consumer(sharedResource);

        producerThread.start();
        consumerThread.start();
    }
}