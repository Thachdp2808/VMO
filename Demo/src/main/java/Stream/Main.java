package Stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        int[] array = {1,2,3,4,5,6,7,8,9,10};

        Stream<Integer> stream = Arrays.stream(array).boxed();

        int sum = Arrays.stream(array).filter(n -> n % 2 == 0).sum();
        System.out.println(sum);
        Arrays.stream(array).filter(n -> n % 2 == 0).forEach(System.out :: println);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        long count = list.stream().count();
        System.out.println(count);



    }
}
