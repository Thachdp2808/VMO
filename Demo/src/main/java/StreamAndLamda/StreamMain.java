package StreamAndLamda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StreamMain {
    public static void main(String[] args) {
        int[] array = {1,2,3,4,5,6,7,8,9,10};
        int[] array1 = {3,2,5,4,7,4,9,3,2,5};

        // Tính tổng số chăn
        int sum = Arrays.stream(array).filter(n -> n % 2 == 0).sum();
        System.out.println(sum);

        // Sorted trên stram là tạo ra một stream mới và thay đổi trên đó
        // Không làm thay đổi mảng gốc
        Arrays.stream(array1).sorted().forEach(System.out :: print);
        for(int i = 0; i < array1.length; i++){
            System.out.println(array1[i]);
        }

        Arrays.stream(array).filter(n -> n % 2 == 0).forEach(System.out :: println);

        // Nó sẽ thực sự filter khi gọi đến hoạt động kết thúc(foreach, sum, count)
        // Nếu không dùng hoạt động kết thúc nó sẽ chỉ tạo và không xử lý gì
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        long count = list.stream().count();
        System.out.println(count);



    }
}
