package Synchronized;

import Synchronized.ShareData;

public class SynchronizedMain {
    public static void main(String[] args) {
        //4 luồng
        ShareData shareData = new ShareData();
        CustomThread customThread1 = new CustomThread(shareData);
        customThread1.start();
        CustomThread customThread2 = new CustomThread(shareData);
        customThread2.start();
        CustomThread customThread3 = new CustomThread(shareData);
        customThread3.start();
    }
}
