package ObjectOriented;

import java.util.Arrays;

public class PrimaryStudent extends Student implements StudentAction,Comparable<PrimaryStudent> {
    private int[] grades;

    public PrimaryStudent(String name, int age, String address, int rollNumber, int[] grades) {
        super(name, age, address, rollNumber);
        this.grades = grades;
    }
    @Override
    public double avg(){
        int sum = 0;
        for (int grade : grades) {
            sum += grade;
        }
        return (double) sum / grades.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimaryStudent that = (PrimaryStudent) o;
        return Arrays.equals(grades, that.grades);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(grades);
    }


    @Override
    public void study() {
        System.out.println(getName() + " Studing.");
    }

    @Override
    public int compareTo(PrimaryStudent o) {
        return Integer.compare(this.getAge(),o.getAge());
    }
}
