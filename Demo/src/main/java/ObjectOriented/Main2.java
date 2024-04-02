package ObjectOriented;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main2 {
    public static void main(String[] args) {

        PrimaryStudent student1 = new PrimaryStudent("Nguyen Van A", 18, "Ha Noi", 10, new int[]{10,9,8});
        PrimaryStudent student2 = new PrimaryStudent("Nguyen Van B", 20, "Ha Noi", 11, new int[]{8,7,8});
        PrimaryStudent student3 = new PrimaryStudent("Nguyen Van C", 17, "Ha Noi", 12, new int[]{7,7,7});
        PrimaryStudent student4 = new PrimaryStudent("Nguyen Van D", 19, "Ha Noi", 13, new int[]{6,7,8});
        PrimaryStudent student5 = new PrimaryStudent("Nguyen Van E", 19, "Ha Noi", 13, new int[]{6,7,8});

        Map<Integer,PrimaryStudent> hashMap = new HashMap<>();
        hashMap.put(1,student2);
        hashMap.put(2,student3);
        hashMap.put(3,student1);
        hashMap.put(4,student4);
        System.out.println("Before replace: ");
        for (Map.Entry<Integer, PrimaryStudent> entry : hashMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue().getName());
        }
        System.out.println("After replace: ");
        hashMap.put(2,student5);
        hashMap.put(5,student5);
        for (Map.Entry<Integer, PrimaryStudent> entry : hashMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue().getName());
        }
        // Thứ tự có thể không đảm bảo

//        Map<Integer, PrimaryStudent> linkedHashMap = new LinkedHashMap<>();
//        linkedHashMap.put(1,student3);
//        linkedHashMap.put(2,student2);
//        linkedHashMap.put(3,student1);
//        linkedHashMap.put(4,student4);
//        System.out.println("Linked Hash Map: ");
//        for (Map.Entry<Integer, PrimaryStudent> entry : linkedHashMap.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue().getName());
//        }
        // Sắp xếp theo thứ tự add vào

        Map<Integer, PrimaryStudent> treeMap = new TreeMap<>();
        treeMap.put(1,student3);
        treeMap.put(3,student2);
        treeMap.put(2,student1);
        treeMap.put(4,student4);
        System.out.println("Linked Hash Map: ");
        for (Map.Entry<Integer, PrimaryStudent> entry : treeMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue().getName());
        }
        // Sắp xếp theo key tăng dần

    }
}
