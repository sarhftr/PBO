package Visibility;

public class Main {
    
    public static void main(String[] args) {
    Person person1 = new Person("Rizka", "Jakarta Selatan", 19, 10);
    person1.displayinfo();

    person1.setName("Rizka");
    person1.setAddress("JAkarta Selatan");
    person1.setAge(19);
    person1.settgllahir(10);

    System.out.println("Updated info");
    person1.displayinfo();
    System.out.println("-----------------------");
    Person person2 = new Person("Bagus", "Jakarta Timur", 20, 17);
    person2.displayinfo();

    person2.setName("Bagus");
    person2.setAddress("Jakarta Timur");
    person2.setAge(20);
    person2.settgllahir(17);

    System.out.println("Updated info");
    person2.displayinfo();
    }
}
