package Synchronized;

import Synchronized.ShareData;

import java.util.Random;

public class CustomThread extends Thread{
    ShareData shareData;

    public CustomThread(ShareData shareData){
        this.shareData = shareData;
    }

    @Override
    public void run() {
        Random random = new Random();
        for(int i = 0; i < 10; i++){
            int number = random.nextInt(5);
            shareData.add(number);
        }
    }
}
