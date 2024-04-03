package Thread;

public class ThreadRunable implements Runnable{
    // Bắt buộc phải có method run từ Runable (functional interface)
    // Sử dụng trong trường hợp chia sẻ code, tái sử dụng lại code
    // Trong class này có thể kế thừa từ nhiều implements khác nữa
    // Giúp logic code dễ đọc và bảo trì hơn
    @Override
    public void run() {
        System.out.print("Thread ");
        for(int i = 0; i < 10; i++){
            if(i % 2 != 0){
                System.out.print(" "+ i);
            }

        }
    }
}
