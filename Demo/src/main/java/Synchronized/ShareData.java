package Synchronized;

public class ShareData {
    int random = 0 ;
    //Synchronized method (Phương thức đồng bộ)
    public synchronized void add(int plus){
        System.out.println("\nDãy số " + plus);
        for (int i = 0; i < plus; i++){
            System.out.print(" " + i);
        }
    }
}
