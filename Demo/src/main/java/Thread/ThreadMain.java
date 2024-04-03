package Thread;

public class ThreadMain {
    private static final int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static int sum = 0;
    private static int count = 0;

    public static void main(String[] args) {
        System.out.println("Start");
        //Việc sử dụng lớp vô danh thì không thể implements từ 1 interface khác nữa
        // Sử dụng lamda expression
        Thread thread = new Thread(() -> {
            for (int num : numbers) {
                sum += num;
            }
            System.out.println("Tổng của mảng là: " + sum);
        });

        //Cách ngắn gọn hơn và nhanh hơn
        new Thread(() -> {
            for (int num : numbers) {
                count++;
            }
            System.out.println("Số luợng của mảng là: " + count);
        }).start();

        // Sử dụng bth
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("Các phần tử của mảng: ");
                for (int i = 0; i < numbers.length; i++) {
                    System.out.print(numbers[i] + " ");
                }
                System.out.print("\n");
            }
        });
        // Bắt đầu thực thi luồng
        // Sử dụng start để bắt đầu luồng
        thread.start();
        thread3.start();

        // 1 luồng là Star - End
        // 1 luồng sum
        // 1 luồng count
        // 3 luồng chạy // và độc lập
        System.out.println("End");

        ThreadDemo threadDemo = new ThreadDemo();
        threadDemo.start();
        ThreadRunable threadRunable = new ThreadRunable();
        threadRunable.run();
    }
}
