package ObjectOriented;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        PrimaryStudent student = new PrimaryStudent("Nguyen Van A", 18, "Ha Noi", 10, new int[]{10,9,8});
//        student.displayInformation();
//        double primaryAvgGrade = student.avg();
//        System.out.println("Primary Student Average Grade: " + primaryAvgGrade);
//        student.study();
//
//        File file = new File();
//        file.readFile();


        PrimaryStudent student1 = new PrimaryStudent("Nguyen Van A", 18, "Ha Noi", 10, new int[]{10,9,8});
        PrimaryStudent student2 = new PrimaryStudent("Nguyen Van B", 20, "Ha Noi", 11, new int[]{8,7,8});
        PrimaryStudent student3 = new PrimaryStudent("Nguyen Van C", 17, "Ha Noi", 12, new int[]{7,7,7});
        PrimaryStudent student4 = new PrimaryStudent("Nguyen Van D", 19, "Ha Noi", 13, new int[]{6,7,8});

        List<PrimaryStudent> list = new ArrayList<>();
        list.add(student1);
        list.add(student2);
        list.add(student3);
        list.add(student4);
//        for (PrimaryStudent item : list) {
//            System.out.println(item.getName());
//        }

        // Sử dụng comparable
//        Collections.sort(list);
//
//        // In ra danh sách đã sắp xếp
//        System.out.println("Sorted student list (by age) by Comparable:");
//        for (PrimaryStudent student : list) {
//            System.out.println(student.getName());
//        }


        // Sử dụng comparator
        Collections.sort(list, new Comparator<PrimaryStudent>() {
            @Override
            public int compare(PrimaryStudent o1, PrimaryStudent o2) {
                return Integer.compare(o1.getAge(),o2.getAge());
            }
        });

        System.out.println("Sorted student list (by age) by Comparator:");
        for (PrimaryStudent student : list) {
            System.out.println(student.getName());
        }

//        Set<PrimaryStudent> hashset = new HashSet<>();
//        hashset.add(student1);
//        hashset.add(student3);
//        hashset.add(student2);
//        hashset.add(student4);
//        for (PrimaryStudent item : hashset) {
//            System.out.println(item.getName());
//        }
        // Không theo thứ tự

//        Set<PrimaryStudent> linkedHashSet = new LinkedHashSet<>();
//        linkedHashSet.add(student2);
//        linkedHashSet.add(student1);
//        linkedHashSet.add(student3);
//        linkedHashSet.add(student4);
//        for (PrimaryStudent item : linkedHashSet) {
//            System.out.println(item.getName());
//        }
        // Theo thứ tự add vào

//        TreeSet<PrimaryStudent> treeSet = new TreeSet<>(new Comparator<PrimaryStudent>() {
//            @Override
//            public int compare(PrimaryStudent o1, PrimaryStudent o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
//        treeSet.add(student2);
//        treeSet.add(student1);
//        treeSet.add(student3);
//        treeSet.add(student4);
//        for (PrimaryStudent item : treeSet) {
//            System.out.println(item.getName());
//        }


    }
}
