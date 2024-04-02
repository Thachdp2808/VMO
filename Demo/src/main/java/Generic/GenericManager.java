package Generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenericManager {
    public static void inList(List<? extends Animal> list){
        for(Animal animal : list){
            animal.eat();
        }
    }

    public static void inCollection(Collection<?> collection){
        for(Object object : collection){
            System.out.println(object);
        }
    }


    public static void main(String[] args) {
        // Gọi object FlasCard với kiểu dữ liệu truyền vào
        FlashCard<String,String> flashCard = new FlashCard<>("Student","Học sinh");
        System.out.println("English: " + flashCard.getKey() + " , " + "Việt Nam: " + flashCard.getValue());

        List<Animal> animalList = new ArrayList<>();
        animalList.add(new Dog());
        animalList.add(new Cat());

        // Gọi phương thức inList với danh sách động vật
        System.out.println("Animals eating:");
        inList(animalList);

        List<Dog> dogList = new ArrayList<>();
        dogList.add(new Dog());
        dogList.add(new Dog());
        dogList.add(new Dog());

        // Gọi phương thức inList với danh sách chó
        System.out.println("\nDogs barking:");
        inList(dogList);

        // Collection với đối tượng là String
        List<String> listString = new ArrayList<>();
        listString.add("abc");
        listString.add("cde");
        listString.add("edc");
        inCollection(listString);

        // Collection với đối tượng là Integer
        List<Integer> listInteger = new ArrayList<>();
        listInteger.add(1);
        listInteger.add(2);
        listInteger.add(3);
        inCollection(listInteger);
    }
}
