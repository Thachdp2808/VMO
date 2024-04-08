package StreamAndLamda;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConsumerMain {
    public static void main(String[] args) {
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("Hello " + s);
            }
        };

        Consumer<String> consumer1 = System.out::println;
        consumer.accept("abc");
        consumer1.accept("bcd");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // Định nghĩa một Consumer để in giá trị của mỗi phần tử
        Consumer<Integer> printValue = (Integer value) -> {
            if(value % 2 == 0){
                System.out.println(value);
            }
        };

        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return false;
            }
        };
//        không chấp nhận bất kỳ đối số nào và trả về một giá trị
//        Supplier<String> randomStringSupplier = () -> "String" + new Random().nextInt();

//        Chấp nhận một đối số và thực hiện một phép biến đổi trên đối số đó để tạo ra một giá trị mới.
//        // Tạo một Function để đếm độ dài của một chuỗi
//        Function<String, Integer> stringLengthFunction = String::length;
//
//        // Lấy một chuỗi ngẫu nhiên từ Supplier
//        String randomString = randomStringSupplier.get();
//
//        // Sử dụng Function để tính độ dài của chuỗi
//        int length = stringLengthFunction.apply(randomString);


        Supplier<String> randomStringSupplier = () ->"" + new Random().nextInt();

        // Tạo một Function để chuyển String -> Integer
        // Truyền vào String và trả về kiểu Integer
        Function<String, Integer> stringLengthFunction = Integer::parseInt;

        // Lấy một chuỗi ngẫu nhiên từ Supplier
        String randomString = randomStringSupplier.get();

        // Sử dụng Function chuyển sang Integer
        Integer length = stringLengthFunction.apply(randomString);

        System.out.println("Random string: " + randomString);
        System.out.println("Length of the string: " + length);

        // Thực thi hành động của Consumer trên mỗi phần tử của danh sách
//        numbers.forEach(printValue);
//         numbers.stream().forEach(printValue);
    }
}
