package StreamAndLamda;

public class LamdaMain {

    public static void main(String[] args) {
        // Biểu thức lamda
        Math add = (a, b) -> a + b;
        Math subtraction = (a, b) -> a - b;

        int resultAdd = add.operate(5,10);
        int resultSub = subtraction.operate(5,10);
        System.out.println(resultSub);
    }
}
