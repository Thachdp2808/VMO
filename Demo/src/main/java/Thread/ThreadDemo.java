package Thread;

public class ThreadDemo extends Thread{
    //Mở rộng class Thread
    //Nếu luồng là luồng chính và không cần chia sẻ hay tái sử dụng
    //Trong một vài trường hợp cần sử dụng đến các method của Thread.
    @Override
    public void run() {
        System.out.print("Thread ");
        for(int i = 0; i < 10; i++){
            System.out.print(" "+ i);
        }
    }
}
