package ObjectOriented;

public abstract class Student extends Person {
    // Tính kế thừa
    // Tính trừu tuợng
    private int rollNumber;

    public Student(String name, int age, String address, int rollNumber){
        super(name, age, address);
        this.rollNumber = rollNumber;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public abstract double avg();
    // Tính đa hình
    @Override
    public void displayInformation() {
       super.displayInformation();
       System.out.println("RollNumber: " + rollNumber);
    }
}
