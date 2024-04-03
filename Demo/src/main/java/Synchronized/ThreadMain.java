package Synchronized;

public class ThreadMain {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        Thread1 thread1 = new Thread1(shareData);
        Thread2 thread2 = new Thread2(shareData);
        thread1.start();
        thread2.start();
    }
}
