package com.everis.reactivex.operators.transforming;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*
Si tiene un <T> finito que emite elementos que implementan <T> comparable, usted
puede usar sorted () para ordenar las emisiones. Internamente, recogerá todas las emisiones y luego
reemitirlos en su orden ordenado. En el siguiente fragmento de código, clasificamos las emisiones
from Observable <Integer> para que se emitan en su orden natural:
 */
public class SortedOperator {

    public static void main(String[] args) {

        Observable.just(6, 2, 5, 7, 1, 4, 9, 8, 3)
                .sorted()
                .subscribe(System.out::println);

        System.out.println("***************** sortedStudentsByAge *****************");
        sortedStudentsByAge();

        System.out.println("***************** reversedOrder *****************");
        reversedOrder();

        System.out.println("***************** orderWithLambda *****************");
        orderWithLambda();
    }

    private static void sortedStudentsByAge() {
        List<Student> students = Arrays.asList(
                new Student("Gian", 31),
                new Student("Ashley", 4),
                new Student("Dina", 29),
                new Student("Ana", 28)
        );

        Observable.fromIterable(students)
                .sorted(Comparator.comparing(Student::getAge))
                .subscribe(
                        s -> System.out.println("Received -> Name: " +
                                s.getName() + "\t\tAge: " + s.getAge())
                );
    }

    private static void reversedOrder() {
        Observable.just(6, 2, 5, 7, 1, 4, 9, 8, 3)
                .sorted(Comparator.reverseOrder())
                .subscribe(System.out::println);
    }

    private static void orderWithLambda() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .sorted((x,y) -> Integer.compare(x.length(), y.length()))
                .subscribe(s -> System.out.println("Received: " + s));
    }


}

class Student {
    private String name;
    private Integer age;

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
