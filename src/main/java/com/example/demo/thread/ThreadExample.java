package com.example.demo.thread;

public class ThreadExample {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new MyThread("任务1", 2));
        MyThread thread2 = new MyThread("任务2", 3);
        MyThread thread3 = new MyThread("任务3", 1);
        MyThread SUN = new MyThread("尿裤子", 4);
        thread1.start();
    }
}
