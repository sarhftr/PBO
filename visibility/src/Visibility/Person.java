package Visibility;

public class Person {
    private String name;
    private String address;
    private int age;
    private int tgllahir;

    public Person(String name, String address, int age, int tgllahir){
        this.name = name;
        this.address = address;
        this.age = age;
        this.tgllahir = tgllahir;
    }    

    public void displayinfo(){
        System.out.println("Name: " + this.name);
        System.out.println("Address: " + this.address);
        System.out.println("Age: " + this.age);
        System.out.println("Tanggal Lahir: " + this.tgllahir);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getAge(){
        return this.age;
    }

    public void settgllahir(int tgllahir){
        this.tgllahir = tgllahir;
    }

    public int gettgllahir(){
        return this.tgllahir;
    }
}