package StackAndHeap;

public class Main {
    public static void main(String[] args) {
        // Khai báo biến kiểu nguyên int trong stack
        int x = 10;

        // Khai báo và khởi tạo một đối tượng String trong heap
        String str = new String("Hello");

        // Hiển thị giá trị của biến x và đối tượng str
        System.out.println("Giá trị của biến x: " + x);
        System.out.println("Giá trị của đối tượng str: " + str);

        // Thêm một đối tượng mới vào heap và gán vào một biến khác
        String newStr = new String("World");

        // Hiển thị giá trị của đối tượng mới và đối tượng str
        System.out.println("Giá trị của đối tượng mới newStr: " + newStr);
        System.out.println("Giá trị của đối tượng str sau khi thêm một đối tượng mới: " + str);
    }
}
