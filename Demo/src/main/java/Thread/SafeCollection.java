package Thread;

import java.util.concurrent.ConcurrentHashMap;

public class SafeCollection {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        // Đặt giá trị trong concurrentMap trong một luồng
        Thread thread1 = new Thread(() -> {
            concurrentMap.put("key1", 1);
        });

        // Lấy giá trị từ concurrentMap trong một luồng khác
        Thread thread2 = new Thread(() -> {
            System.out.println(concurrentMap.get("key1"));
        });

        // Khởi động các luồng
        thread1.start();
        thread2.start();
    }
}
