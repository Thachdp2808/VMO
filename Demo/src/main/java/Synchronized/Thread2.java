package Synchronized;

import java.util.Random;

public class Thread2 extends Thread {
    ShareData shareData;

    public Thread2(ShareData shareData) {
        this.shareData = shareData;
    }
    @Override
    public void run() {
        Random random = new Random();
        for(int i = 0; i < 10; i++){
            //Synchronized block (Khối đồng bộ)
            synchronized (shareData){
                int number = shareData.random * shareData.random;
                System.out.println("T2: " + number);
                // Nếu T2 không đánh thức thì T1 chạy xong sẽ dừng
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
