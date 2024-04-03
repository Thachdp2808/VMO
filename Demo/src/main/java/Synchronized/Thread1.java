package Synchronized;

import java.util.Random;

public class Thread1 extends Thread{
    ShareData shareData;

    public Thread1(ShareData shareData) {
        this.shareData = shareData;
    }

    @Override
    public void run() {
        Random random = new Random();
        for(int i = 0; i < 10; i++){
            //Synchronized block (Khối đồng bộ)
            synchronized (shareData){
                int number = random.nextInt(10);
                shareData.random = number;
                System.out.println("T1: " + number);
                shareData.notify();
                //Tránh bị deadlock
                if(i == 9){
                    stop();
                }
                try {
                    shareData.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
