package com.example.demo.thread;

class MyThread implements Runnable {
    private String name;
    private int duration; // in seconds

    public MyThread(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    @Override
    public void run() {
        try {
            System.out.println("开始处理任务: " + name);
            Thread.sleep(duration * 1000); // Simulate task processing time
            System.out.println("完成任务: " + name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


